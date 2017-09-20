package in.myinnos.awesomeimagepicker.helpers;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by longdg on 2017-08-05.
 */

public class Singleton {
    private static Singleton instance = null;
    public ArrayList<Image> listImageUpdated = new ArrayList<>();
    public boolean isUpdated = false;
    public int positionNeedToUpdate = -1;
    public static Singleton getGetInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
