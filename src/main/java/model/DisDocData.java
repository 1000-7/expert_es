package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Roger
 * @date 2018/3/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisDocData extends DocData{
    
    private Map<Integer, YearData> data;

    public DisDocData(String docId, Map<Integer, YearData> data) {

        this.data = data;
    }
}
