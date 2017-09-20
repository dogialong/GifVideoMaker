package com.gifmaker.gifeditor.ui.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by longdg on 2017-07-31.
 */

public class GridLayoutManagerCustom extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public GridLayoutManagerCustom(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}