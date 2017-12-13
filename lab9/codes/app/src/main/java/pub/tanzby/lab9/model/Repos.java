package pub.tanzby.lab9.model;

/**
 * Created by tan on 2017/12/12.
 */

public class Repos {
    private String name;
    private String description;
    private String language;

    public String getTitle() {
        return name;
    }

    public String getSub2() {
        if(description==null ||  description.isEmpty()) return "Nothing";
        return description;
    }

    public String getSub1() {
        if (language==null || language.isEmpty()) return "Unknown";
        return language;
    }
}
