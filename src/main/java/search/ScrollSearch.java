package search;

import client.ClientFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Roger
 * @date 2018/3/6.
 */
public class ScrollSearch {
    private static Logger log = LoggerFactory.getLogger(ScrollSearch.class);

    public static List<SearchHit> searchByScrollId(String scrollId) {
        List<SearchHit> searchHitList = new ArrayList<>();
        TimeValue timeValue = new TimeValue(10, TimeUnit.MINUTES);
        SearchScrollRequestBuilder searchScrollRequestBuilder;
        SearchResponse response;
        // 结果
        while (true) {
            searchScrollRequestBuilder = ClientFactory.get().prepareSearchScroll(scrollId);
            // 重新设定滚动时间
            searchScrollRequestBuilder.setScroll(timeValue);
            // 请求
            response = searchScrollRequestBuilder.get();
            // 每次返回下一个批次结果 直到没有结果返回时停止 即hits数组空时
            if (response.getHits().getHits().length == 0) {
                break;
            }
            searchHitList.addAll(Arrays.asList(response.getHits().getHits()));
            if (searchHitList.size()>=100){
                System.out.println("dsf"+searchHitList.size());
                return searchHitList;
            }
            log.info("@@scrollId:{}", scrollId);
            // 只有最近的滚动ID才能被使用
            scrollId = response.getScrollId();
        }
        return searchHitList;
    }
}
