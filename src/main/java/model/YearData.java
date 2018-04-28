package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roger
 * @date 2018/3/19.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearData {
    private int year;
    private List<Double> similarity = new ArrayList<>();
    private int count = 0;
    private int fosCount = 0;
    private int issnCount = 0;

    public YearData(int year) {
        this.year = year;
    }

    public void incrCount() {
        count++;
    }

    public void incrFosCount() {
        fosCount++;
    }

    public void incrIssnCount() {
        issnCount++;
    }
}
