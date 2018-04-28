package bean;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpertFieldTest {
    @Test
    public void method() throws IOException {
//        ExpertField ef = new ExpertField();
//        ef.setName("李明");
//        List<Keyword> keywords = new ArrayList<>();
//        keywords.add(new Keyword("信息检索",3));
//        keywords.add(new Keyword("机器学习",2));
//        ef.setKeywords(keywords);
//        ef.setField("管理科学");
//        System.out.println(ef.toString());
        List<String> exp=FileUtils.readLines(new File("/Users/unclewang/Idea_Projects/expert/src/main/resources/经济学.txt"),"UTF-8");
        List<ExpertField> experts = new ArrayList<>();
        for (String s:exp){
            String [] a= s.split("\t");
            ExpertField ef = new ExpertField();
            ef.setField(a[3]);
            List<Keyword>kw= new ArrayList<>();
            for (String x:a[4].split("[、;]")){
                Keyword k=new Keyword(x,5);
                kw.add(k);
            }
            ef.setKeywords(kw);
            ef.setName(a[1]);
            ef.setId(Integer.parseInt(a[0]));
            experts.add(ef);
        }

        System.out.println(experts.toString());
    }

}