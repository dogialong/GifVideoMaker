package in.myinnos.awesomeimagepicker.models;

import java.io.Serializable;

/**
 * Created by MyInnos on 03-11-2016.
 */
public class Album implements Serializable{
    public String name;
    public String cover;

    public Album(String name, String cover) {
        this.name = name;
        this.cover = cover;
    }
}
