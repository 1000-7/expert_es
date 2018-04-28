package bean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
/**
 * @Author unclewang
 * @Date 2018/4/26 16:13
 *
 * 专家名字返回 经常使用的关键词 和所在领域
 */
public class ExpertField {
    private int id;
    private String name;
    private List<Keyword> keywords;
    private String field;


    public ExpertField() {
    }

    public ExpertField(String name) {
        this.name = name;
    }

    public ExpertField(String name, List<Keyword> keywords, String field) {
        this.name = name;
        this.keywords = keywords;
        this.field = field;
    }

    public ExpertField(int id, String name, List<Keyword> keywords, String field) {
        this.id = id;
        this.name = name;
        this.keywords = keywords;
        this.field = field;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        JSONArray keywords = JSONArray.fromObject(this.keywords);
        JSONObject expertField = new JSONObject();
        expertField.put("name", this.name);
        expertField.put("keywords", keywords);
        expertField.put("field", this.field);
        return expertField.toString();
    }
}
