package com.stickerview;

import android.view.MotionEvent;

/**
 * Created by linh on 8/21/2017.
 */

public class EditTextAddIconEvent implements StickerIconEvent {
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
      if (stickerView.getOnStickerOperationListener() != null) {
            stickerView.getOnStickerOperationListener()
                    .onEditTextAdd(stickerView.getCurrentSticker());
        }
    }
}
