package com.gifmaker.gifeditor.ui.view;

/**
 * Created by mr.logic on 7/31/2017.
 */

public interface GifMakeViewUser {

    public void showLoading();
    public void showProgres(int percent);
    public void hidLoading();
    public void showError(String error);
    public void showSuccess(String msg);

}
