package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Roger
 * @date 2018/3/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doc {

    private String id;
    private int ncitation;
    private List<String> references;
    private int year;
    private List<String> foses;
    private String issn;

    public Doc(String id, int year) {
        this.id = id;
        this.year = year;
    }

    public Doc(String id, int year, List<String> foses, String issn) {
        this.id = id;
        this.year = year;
        this.foses = foses;
        this.issn = issn;
    }
}
