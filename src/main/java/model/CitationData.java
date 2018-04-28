package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Roger
 * @date 2018/4/7.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitationData extends DocData{

    private Map<String, Double> similarities = new HashMap<>();

    public CitationData(String docId) {
        super();
    }
}
