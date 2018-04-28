package bean;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpertRelationTest {

    @Test
    public void test() throws IOException {
        List<String> exp=FileUtils.readLines(new File("/Users/unclewang/Idea_Projects/expert/src/main/resources/经济学.txt"),"UTF-8");
        List<Expert> experts = new ArrayList<>();
        for (String s:exp){
            String [] a= s.split("\t");
            Expert expert = new Expert(Integer.parseInt(a[0]), a[1], "男", 34, a[2], a[3],100);
            experts.add(expert);
        }

        ExpertRelation er=new ExpertRelation();
        er.setName("姜波克");
        er.setRelationExperts(experts);
        System.out.println(er.toString());
    }
}