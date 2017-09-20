package com.gifmaker.gifeditor.ui.view;

/**
 * Created by longdg on 2017-08-15.
 */

public  interface OnGetVideoListener {
    void sucsess(String path);
    void error();
    void onprogress(String path);
}
