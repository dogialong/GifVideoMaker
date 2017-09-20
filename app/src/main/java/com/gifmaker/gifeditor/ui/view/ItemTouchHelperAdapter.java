package com.gifmaker.gifeditor.ui.view;

/**
 * Created by longdg on 2017-08-16.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
