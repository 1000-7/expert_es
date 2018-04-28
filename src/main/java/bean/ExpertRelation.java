package bean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * @Author unclewang
 * @Date 2018/4/26 13:32
 *
 * 专家 返回 其相关社会关系的专家列表
 */
public class ExpertRelation {
    private final static int SIZE = 100;
    private String name;
    private List<Expert> relationExperts = null;

    public ExpertRelation() {
    }

    public ExpertRelation(String name, List<Expert> relationExperts) {
        this.name = name;
        this.relationExperts = relationExperts;
    }

    public static int getSIZE() {
        return SIZE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expert> getRelationExperts() {
        return relationExperts;
    }


    public void setRelationExperts(List<Expert> relationExperts) {
        if (relationExperts.size() < SIZE + 1) {
            this.relationExperts = relationExperts;
        } else {
            this.relationExperts = relationExperts.subList(0, SIZE);
        }

    }

    @Override
    public String toString() {
        JSONArray jsonArray = JSONArray.fromObject(this.relationExperts);
        JSONObject expert = new JSONObject();
        expert.put("name", this.name);
        expert.put("relations", jsonArray);
        return expert.toString();
    }
}
