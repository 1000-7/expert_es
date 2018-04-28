package bean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
/**
 * @Author unclewang
 * @Date 2018/4/26 13:39
 *
 * 关键词 返回其相关的专家列表
 */
public class ExpertKeyWord {
    private final static int SIZE = 100;
    private String keyword;
    private List<Expert> experts;

    public ExpertKeyWord() {
    }

    public ExpertKeyWord(String keyword, List<Expert> experts) {
        this.keyword = keyword;
        this.experts = experts;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Expert> getExperts() {
        return experts;
    }

    public void setExperts(List<Expert> experts) {
        if (experts.size() < SIZE + 1) {
            this.experts = experts;
        } else {
            this.experts = experts.subList(0, SIZE);
        }
    }

    @Override
    public String toString() {
        JSONArray jsonArray = JSONArray.fromObject(this.experts);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("keyword", this.keyword);
        jsonObject.put("experts", jsonArray);
        return jsonObject.toString();
    }
}
