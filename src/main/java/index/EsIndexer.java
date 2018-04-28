package index;

import client.ClientFactory;
import config.Global;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author Roger
 * @date 2018/1/20.
 */
public class EsIndexer {
    private Settings.Builder settings = Settings.builder()
            .put("index.number_of_shards", Global.getEsNumOfShards())
            .put("index.number_of_replicas", Global.getEsNumOfReplicas());

    protected static Client client = ClientFactory.get();

    protected String indice;
    protected String type;
    protected String filepath;

    public EsIndexer(String indice, String type, String filepath) {
        this.indice = indice;
        this.type = type;
        this.filepath = filepath;
    }

    public void index() throws IOException, InterruptedException {
        checkIndex(indice, type);
        doIndex();
    }

    private void doIndex() throws InterruptedException {
        BulkProcessor bulkProcessor = getBulkProcessor();
        File file = new File(filepath);
        if (file.exists()) {
            processFile(file, bulkProcessor);
            bulkProcessor.flush();
            bulkProcessor.awaitClose(5, TimeUnit.MINUTES);
        } else {
            System.out.println(filepath + " doesn't exist!");
        }
    }

    private void processFile(File file, BulkProcessor bulkProcessor) {
        try {
            if (file.isFile()) {
                // 需要处理的是单个文件
                System.out.println("@@indexing@@, [" + file.getCanonicalPath() + "]");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String record = null;
                while ((record = reader.readLine()) != null) {
                    JSONObject json = JSONObject.parseObject(record);
                    bulkProcessor.add(upsert(json.getString("id"), record));
                }
            } else {
                for (File f : file.listFiles()) {
                    processFile(f, bulkProcessor);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建索引map
     *
     * @throws IOException
     */
    protected void map() throws IOException {
        PutMappingResponse response = client.admin().indices().preparePutMapping(indice)
                .setType(type)
                .setSource(
                        jsonBuilder()
                                .startObject()
                                .startObject("properties")
                                .startObject("id").field("type", "keyword").field("store", true).endObject()
                                .startObject("title").field("type", "text").field("store", true).endObject()
                                .startObject("authors").field("type", "nested")
                                .startObject("properties")
                                .startObject("name").field("type", "text").field("store", true).endObject()
                                .startObject("org").field("type", "text").endObject()
                                .endObject()
                                .endObject()
                                .startObject("venue").field("type", "text").endObject()
                                .startObject("year").field("type", "integer").field("store", true).endObject()
                                .startObject("keywords").field("type", "text").field("store", true).endObject()
                                .startObject("fos").field("type", "text").field("store", true)
                                    .startObject("fields").startObject("raw").field("type", "keyword").endObject().endObject()
                                .endObject()
                                .startObject("n_citation").field("type", "integer").field("store", true).endObject()
                                .startObject("references").field("type", "keyword").endObject()
                                .startObject("page_stat").field("type", "integer").endObject()
                                .startObject("page_end").field("type", "integer").endObject()
                                .startObject("doc_type").field("type", "keyword").field("store", true).endObject()
                                .startObject("lang").field("type", "keyword").field("store", true).endObject()
                                .startObject("publisher").field("type", "text").endObject()
                                .startObject("volume").field("type", "text").endObject()
                                .startObject("issue").field("type", "text").endObject()
                                .startObject("issn").field("type", "keyword").endObject()
                                .startObject("isbn").field("type", "keyword").endObject()
                                .startObject("doi").field("type", "keyword").endObject()
                                .startObject("pdf").field("type", "keyword").endObject()
                                .startObject("url").field("type", "keyword").endObject()
                                .startObject("abstract").field("type", "text").endObject()
                                .endObject()
                                .endObject()).get();
        System.out.println("@@put mapping@@, ack:" + response.isAcknowledged());
    }

    /**
     * 检验索引是否存在
     * 不存在才创建
     * @param indice
     * @param type
     * @throws IOException
     */
    private void checkIndex(String indice, String type) throws IOException {
        if (!isExistsIndex(indice)) {
            createIndex(indice);
        }
        if (!isExistsType(indice, type)) {
            map();
        }
    }

    /**
     * 给索引添加别名
     *
     * @param indice
     * @param alias
     * @return
     */
    private boolean addAlias(String indice, String alias) {
        IndicesAliasesResponse rsp = client.admin().indices().prepareAliases().addAlias(indice, alias).get();
        return rsp.isAcknowledged();
    }

    /**
     * 删除索引对应的别名
     *
     * @param indice
     * @param alias
     * @return
     */
    private boolean removeAlias(String indice, String alias) {
        IndicesAliasesResponse rsp = client.admin().indices().prepareAliases().removeAlias(indice, alias).get();
        return rsp.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param indice
     * @return
     */
    private boolean deleteIndex(String indice) {
        return client.admin().indices().prepareDelete(indice).get().isAcknowledged();
    }

    /**
     * 判断指定的索引名是否存在
     *
     * @param indexName 索引名
     * @return 存在：true; 不存在：false;
     */
    private boolean isExistsIndex(String indexName) {
        IndicesExistsResponse response =
                client.admin().indices().exists(
                        new IndicesExistsRequest().indices(indexName)).actionGet();
        return response.isExists();
    }

    /**
     * 判断指定的索引的类型是否存在
     *
     * @param type 索引类型
     * @return 存在：true; 不存在：false
     */
    private boolean isExistsType(String indice, String type) {
        TypesExistsResponse response =
                client.admin().indices()
                        .typesExists(new TypesExistsRequest(new String[]{indice}, type))
                        .actionGet();
        return response.isExists();
    }

    /**
     * 创建索引
     *
     * @param indice
     * @return
     */
    private boolean createIndex(String indice) {
        CreateIndexResponse rsp = client.admin().indices().prepareCreate(indice)
                .setSettings(settings)
                .get();
        return rsp.isAcknowledged();
    }

    protected UpdateRequest upsert(String id, String source) {
        return new UpdateRequest(indice, type, id).doc(source, XContentType.JSON).upsert(source, XContentType.JSON);
    }

    /**
     * 获取 BulkProcessor
     *
     * @return
     */
    protected static BulkProcessor getBulkProcessor() {
        return BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                        System.out.println("@@bulk success@@, execId:" + executionId + ", bulk size:" + response.getItems().length);
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        System.out.println("@@bulk error@@, execId:" + executionId + ", failure:" + failure.getMessage());
                    }
                })
                .setBulkActions(Global.getEsBulkActions())
                .setBulkSize(new ByteSizeValue(Global.getEsBulkSize(), ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(5)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }
}
