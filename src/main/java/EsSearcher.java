import client.ClientFactory;
import config.Global;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 查询类
 * @author Roger
 * @date 2018/2/24.
 */
public class EsSearcher {
    
    protected static Client client = ClientFactory.get();

    public static void searchFos(String indice, String type) {
        System.out.println("start to search " + Global.getEsTransportAddress() + indice + "/" + type +" [fos]!");
        Set<Object> set = new TreeSet<>();
        // 构造es的match_all查询,用于匹配所有的记录
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        // 开始查询es索引
        SearchResponse response = client.prepareSearch(indice)
                .setTypes(type)
                .setQuery(queryBuilder)
                .setFetchSource(new String[]{"fos"}, null).get();
        System.out.println("Total Hits:" + response.getHits().totalHits);
        for (SearchHit hit : response.getHits().getHits()) {
            // 取出每条记录的fos值并添加到set中进行去重
            Object list = hit.getSourceAsMap().get("fos");
            if (list != null && list instanceof List) {
                set.addAll((Collection<?>) list);
            }
        }
        System.out.println("***********");
        // 输出所有去重后的fos值
        for (Object o : set) {
            System.out.println(o);
        }
    }

    public static void main(String[] args) {
        // str输入可以是: aminer(该数据集不存在fos值) 或者 mag
        String str = "mag";
        searchFos(str, str);
    }
}
