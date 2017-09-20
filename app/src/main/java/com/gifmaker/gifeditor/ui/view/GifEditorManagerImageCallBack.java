package com.gifmaker.gifeditor.ui.view;

/**
 * Created by PhungVanQuang on 8/1/2017.
 */

public interface GifEditorManagerImageCallBack {

    void onUpdateList();
    void onRemoveList();
    boolean hideList(boolean isHide);
}
