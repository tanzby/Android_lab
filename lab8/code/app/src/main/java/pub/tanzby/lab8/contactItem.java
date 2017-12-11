package pub.tanzby.lab8;

import java.io.Serializable;

/**
 * Created by tan on 2017/12/5.
 */

public class contactItem implements Serializable {
    String name;
    String birthday;
    String gift;
    contactItem()
    {
        name=birthday = gift = "";
    }
    contactItem(String name,String birthday, String gift)
    {
        this.name = name;
        this.birthday = birthday;
        this.gift = gift;
    }

}
