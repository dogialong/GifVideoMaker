package com.stickerview;

import android.view.MotionEvent;

/**
 * Created by linh on 8/23/2017.
 */

public class RotateIconEvent implements StickerIconEvent {
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {
        stickerView.rotateCurrentSticker(event);
    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {

    }
}
