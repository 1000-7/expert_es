package search;

import client.ClientFactory;
import config.Global;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Roger
 * @date 2018/3/6.
 */
public class NcitationSearcher {
    private static Logger log = LoggerFactory.getLogger(NcitationSearcher.class);

    /**
     * 查询 n_citation >= threshold的文档id
     *
     * @param index
     * @param type
     * @return docIds
     */
    public static Set<String> search(String index, String type) {
        SearchResponse response = ClientFactory.get().prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.rangeQuery("n_citation")
                        .gte(Global.getNcitationThreshold()))
                .setFetchSource(false)
                .setScroll(new TimeValue(10, TimeUnit.MINUTES))
                .setSize(3000)
                .get();
        String scrollId = response.getScrollId();
        log.info("@scrollId:{}", scrollId);
        SearchHit[] searchHits = response.getHits().getHits();
        Set<String> docIds = new HashSet<>(searchHits.length);
        for (SearchHit hit : searchHits) {
            docIds.add(hit.getId());
        }
        for (SearchHit hit : ScrollSearch.searchByScrollId(scrollId)) {
            docIds.add(hit.getId());
        }
        log.info("@@NcitationRangeSearch.size:{}", docIds.size());
        return docIds;
    }

    public static Set<String> searchWithFos(String index, String type, String fos) {
        SearchResponse response = ClientFactory.get().prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.termQuery("fos", fos))
                .setFetchSource(false)
                .setScroll(new TimeValue(5, TimeUnit.MINUTES))
                .setSize(3000)
                .addSort("n_citation", SortOrder.DESC)
                .get();
        String scrollId = response.getScrollId();
        log.info("@scrollId:{}", scrollId);
        SearchHit[] searchHits = response.getHits().getHits();
        Set<String> docIds = new HashSet<>(searchHits.length);
        for (SearchHit hit : searchHits) {
            docIds.add(hit.getId());
        }
        for (SearchHit hit : ScrollSearch.searchByScrollId(scrollId)) {
            docIds.add(hit.getId());
        }
        log.info("@@Fos topN.size:{}", docIds.size());
        return docIds;
    }

    public static Set<String> searchTopWithFos(String index, String type, String fos, int size) {
        SearchResponse response = ClientFactory.get().prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.matchPhraseQuery("fos", fos))
                .setFetchSource(false)
                .setScroll(new TimeValue(5, TimeUnit.MINUTES))
                .setSize(size)
                .addSort("n_citation", SortOrder.DESC)
                .get();
        SearchHit[] searchHits = response.getHits().getHits();
        Set<String> docIds = new HashSet<>(searchHits.length);
        for (SearchHit hit : searchHits) {
            docIds.add(hit.getId());
        }
        log.info("@@Fos topN.size:{}", docIds.size());
        return docIds;
    }
}
