package com.gifmaker.gifeditor.ui.view;

import android.graphics.Bitmap;

/**
 * Created by longdg on 2017-08-08.
 */

public interface ImageViewEffectCallBack {

    public void displayImage(Bitmap bitmap);
    public void displayError(String msg);
    public void displayLoading();
}
