package com.gifmaker.gifeditor.model;

/**
 * Created by linh on 8/5/17.
 */

public class ItemFontText {
    public ItemFontText(){

    }
    public String getNameFont() {
        return nameFont;
    }

    public void setNameFont(String nameFont) {
        this.nameFont = nameFont;
    }

    private String nameFont = "";

    public ItemFontText(String nameFont) {
        this.nameFont = nameFont;
    }
}
