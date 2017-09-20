package in.myinnos.awesomeimagepicker.activities.fragment;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by PhungVanQuang on 8/17/2017.
 */

public interface ImageSelectCallBack {

    public void doneSelect();
    public void listImage(ArrayList<Image> imagesSelect);

}
