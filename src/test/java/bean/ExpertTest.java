package bean;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExpertTest {
    @Test
    public void test() {
        List<Expert>experts=new ArrayList<>();
        for (int id = 0; id < 10; id++) {
            Expert expert = new Expert(id, "黄达", "男", 93, "中国人民大学", "金融学");
            experts.add(expert);
        }
        JSONArray jsonArray=JSONArray.fromObject(experts);
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("experts",jsonArray);
        System.out.println(jsonObject.toString());
    }
}