package pub.tanzby.lab9.model;

/**
 * Created by tan on 2017/12/12.
 * 用于承接Github的用户基本数据
 */

public class GitHub {
    private String login;
    private String blog;
    private String id;

    public GitHub(String id, String blog, String login){
        this.id=id;
        this.blog=blog;
        this.login=login;
    }

    public String getSub1(){return id;}
    public String getSub2() {return blog;}
    public String getTitle()   {return login;}
}
