package bean;

import net.sf.json.JSONObject;
/**
 * @Author unclewang
 * @Date 2018/4/25 12:03
 *
 * 专家类
 */
public class Expert {
    private Integer aid;
    private String name;
    private String gender;
    private Integer age;
    private String university;
    private String subject;
    private Integer relationNum;

    public Expert() {
    }

    public Expert(Integer id, String name, String gender, Integer age, String university, String subject) {
        this.aid = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.university = university;
        this.subject = subject;
    }

    public Expert(Integer id, String name, String gender, Integer age, String university, String subject, Integer relationNum) {
        this.aid = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.university = university;
        this.subject = subject;
        this.relationNum = relationNum;
    }



    public Integer getId() {
        return aid;
    }

    public void setId(Integer id) {
        this.aid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getRelationNum() {
        return relationNum;
    }

    public void setRelationNum(Integer relationNum) {
        this.relationNum = relationNum;
    }

    @Override
    public String toString() {
        JSONObject expert = JSONObject.fromObject(this);
        return expert.toString();
    }
}
