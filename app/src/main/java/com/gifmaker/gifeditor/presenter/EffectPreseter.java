package com.gifmaker.gifeditor.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.gifmaker.gifeditor.model.controller.EffectHelper;
import com.gifmaker.gifeditor.ui.view.ImageViewEffectCallBack;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class EffectPreseter extends BasePresenter {

    private EffectHelper effectHelper;

    public EffectPreseter() {
        effectHelper = new EffectHelper();
    }

    public void makeEffect(Context context,int indexListFrameImage,int indexBitmapFrameImage, int widthImageView, int heightImageView, Bitmap allBitMapAddToGif, ArrayList<String> listPathImages,final GPUImageFilter effectCurrentImage, final GPUImageFilter brightNessImage, EffectHelper.EffectsImageCallBack effectsImageCallBack) {
        effectHelper.makeEffectFromListImage(context,indexListFrameImage,indexBitmapFrameImage,allBitMapAddToGif,listPathImages,widthImageView,heightImageView,effectCurrentImage,brightNessImage,effectsImageCallBack);
    }

    public void makeEffectForImage(final ImageViewEffectCallBack imageViewEffectCallBack, final String pathImage, GPUImageFilter effectCurrentImage, final GPUImageFilter brightNess) {

            effectHelper.makeEffectForImage(pathImage,effectCurrentImage, brightNess, new EffectHelper.OnEffectHelper() {
            @Override
            public void onEffectStart() {
                imageViewEffectCallBack.displayLoading();
            }

            @Override
            public void onEffectSuccess(Bitmap bitmap) {
                imageViewEffectCallBack.displayImage(bitmap);
            }

            @Override
            public void onEffectError(String msg) {
                imageViewEffectCallBack.displayError(msg);
            }
        });
    }

    public void makeEffectForImage(ImageView imageView, final String pathImage, final GPUImageFilter effectCurrent, final GPUImageFilter brightness) {
        imageView.setImageBitmap(effectHelper.makeEffectForImage(pathImage, effectCurrent, brightness));
    }

}
