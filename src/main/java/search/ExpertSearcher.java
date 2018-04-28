package search;

import client.ClientFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ExpertSearcher {

    public static Set<String> search(String index, String type) {
        SearchResponse response = ClientFactory.get().prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.boolQuery().filter(QueryBuilders.existsQuery("fos"))
                        .must(QueryBuilders.termQuery("lang", "zh_chs")))
                .setFetchSource(new String[]{"id", "keywords", "authors", "fos"}, null)
                .setScroll(new TimeValue(10, TimeUnit.MINUTES))
                .setSize(10)
                .get();
        String scrollId = response.getScrollId();
//        System.out.println("sdfhksj" + scrollId);
        SearchHit[] searchHits = response.getHits().getHits();
        Map<String, Object> source;
        Set<String> docIds = new HashSet<>(searchHits.length);
        for (SearchHit hit : searchHits) {
            System.out.println(hit.getId());
            source = hit.getSourceAsMap();
            System.out.println(source.toString());
            docIds.add(hit.getId());
        }

        List<SearchHit> xa = ScrollSearch.searchByScrollId(scrollId);
        System.out.println("前10个打印" + xa.size());
        for (SearchHit hit : xa) {
            docIds.add(hit.getId());
        }
        return docIds;
    }

    public static Set<String> searchName(String index, String type, String name) {
        SearchResponse response = ClientFactory.get().prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.boolQuery().filter(QueryBuilders.existsQuery("fos"))
                        .must(QueryBuilders.termQuery("lang", "zh_chs")))
                .setFetchSource(false)
                .setScroll(new TimeValue(10, TimeUnit.MINUTES))
                .setSize(10)
                .get();
        String scrollId = response.getScrollId();
        System.out.println("sdfhksj" + scrollId);
        SearchHit[] searchHits = response.getHits().getHits();
        Set<String> docIds = new HashSet<>(searchHits.length);
        for (SearchHit hit : searchHits) {
            docIds.add(hit.getId());
        }
        System.out.println(docIds.size());
        System.out.println("前10个打印");
        for (SearchHit hit : ScrollSearch.searchByScrollId(scrollId)) {
            docIds.add(hit.getId());
        }
        System.out.println(docIds.size());
        return docIds;
    }

    public static Set<String> searchfoskeywords(String index, String type) {
        SearchResponse response = ClientFactory.get().prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.boolQuery().filter(QueryBuilders.existsQuery("keywords")))
                        .must(QueryBuilders.matchQuery("lang", "zh_chs"))
                        .must(QueryBuilders.boolQuery().filter(QueryBuilders.existsQuery("fos")))
                        .must(QueryBuilders.matchQuery("year", "2014"))
                        .must(QueryBuilders.matchQuery("issue", "8"))
                        .must(QueryBuilders.matchQuery("page_end", "766"))

                )
                .setFetchSource(true)
                .setScroll(new TimeValue(10, TimeUnit.MINUTES))
                .setSize(10)
                .get();
        String scrollId = response.getScrollId();
//        System.out.println("sdfhksj" + scrollId);
        SearchHit[] searchHits = response.getHits().getHits();
        Map<String, Object> source;
        Set<String> docIds = new HashSet<>(searchHits.length);
        for (SearchHit hit : searchHits) {
            System.out.println(hit.getId());
            source = hit.getSourceAsMap();
            System.out.println(source.toString());
            docIds.add(hit.getId());
        }

        List<SearchHit> xa = ScrollSearch.searchByScrollId(scrollId);
        System.out.println("前10个打印" + xa.size());
        for (SearchHit hit : xa) {
            docIds.add(hit.getId());
        }
        return docIds;
    }

    public static void main(String[] args) {
        Set<String> result = searchfoskeywords("mag", "mag");
        System.out.println(result.size());
        for (String s : result) {
            System.out.println(s);
        }
    }
}
