package search;

import client.ClientFactory;
import model.Doc;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Roger
 * @date 2018/3/6.
 */
public class DocSearcher {


    /**
     * 根据docId返回文档信息
     *
     * @param index
     * @param type
     * @param docId
     * @return
     */
    public static Doc search(String index, String type, String docId) {
        GetResponse response = ClientFactory.get().prepareGet(index, type, docId)
                .setFetchSource(new String[]{"year", "fos", "venue"}, null)
                .get();
        Map<String, Object> source = response.getSourceAsMap();
        return doc(docId, source);
    }

    /**
     * 查询文章的引文
     *
     * @param index
     * @param type
     * @param docId
     * @return 返回引文的id, year
     */
    public static List<Doc> searchCitation(String index, String type, String docId) {
        long start = System.currentTimeMillis();
        SearchResponse response = ClientFactory.get().prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.termQuery("references", docId))
                .setFetchSource(new String[]{"year", "fos", "venue"}, null)
                .addSort("year", SortOrder.ASC)
                .setScroll(new TimeValue(5, TimeUnit.MINUTES))
                .setSize(3000)
                .get();
        String scrollId = response.getScrollId();
        SearchHit[] searchHits = response.getHits().getHits();
        List<Doc> citations = new ArrayList<>();
        Map<String, Object> source;
        for (SearchHit hit : searchHits) {
            source = hit.getSourceAsMap();
            citations.add(doc(hit.getId(), source));
        }
        for (SearchHit hit : ScrollSearch.searchByScrollId(scrollId)) {
            source = hit.getSourceAsMap();
            citations.add(doc(hit.getId(), source));
        }
        return citations;
    }

    private static Doc doc(String docId, Map<String, Object> source) {
        int year = source.containsKey("year") ? (Integer) source.get("year") : 0;
        List<String> foses = source.containsKey("fos") ? (List<String>) source.get("fos") : null;
        String issn = source.containsKey("venue") ? (String) source.get("venue") : null;
        return new Doc(docId, year, foses, issn);
    }

}
