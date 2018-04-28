package search;

import client.ClientFactory;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.termvectors.TermVectorsResponse;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roger
 * @date 2018/3/4.
 */
public class TermVectorSearcher {
    private static Logger log = LoggerFactory.getLogger(TermVectorSearcher.class);

    /**
     * 获取文档想关域的termvector
     *
     * @param index
     * @param type
     * @param docId
     * @param fieldName
     * @return key:term,value:freq
     * @throws IOException
     */
    public static Map<String, Long> search(
            String index, String type, String docId, String fieldName) {
        long start = System.currentTimeMillis();
        Map<String, Long> vector = null;
        try {
            TermVectorsResponse resp = ClientFactory.get().prepareTermVectors()
                    .setIndex(index).setType(type).setId(docId)
                    .setSelectedFields(fieldName)
                    .execute().actionGet();
            XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON);
            resp.toXContent(builder, ToXContent.EMPTY_PARAMS);
            Fields fields = resp.getFields();
            Terms terms = fields.terms(fieldName);
            if (terms != null) {
                TermsEnum termsEnum = terms.iterator();
                vector = new HashMap<>();
                while (termsEnum.next() != null) {
                    BytesRef term = termsEnum.term();
                    if (term != null) {
                        vector.put(term.utf8ToString(), termsEnum.totalTermFreq());
                    }
                }
            }
        } catch (Exception e) {
            log.error("@@TermVector@@, docId:{}, msg:{}", docId, e.getMessage());
        }
        log.info("TermVector time: {}", System.currentTimeMillis() - start);
        return vector;
    }
}
