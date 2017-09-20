package com.gifmaker.gifeditor.model.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;


/**
 * Created by PhungVanQuang on 8/2/2017.
 */

public class EffectHelper {

    public EffectHelper() {

    }

    public void makeEffectFromListImage(Context context, int indexListFrameImage, int indexBitmapFrameImage, Bitmap allBitMapAddToGif, final ArrayList<String> listPathImages, int widthImageView, int heightImage, final GPUImageFilter effect, final GPUImageFilter brightNessImage, EffectsImageCallBack effectsImageCallBack) {

    }


    public Bitmap makeEffectForImage(Bitmap bitmap, final GPUImageFilter effectCache, final GPUImageFilter brightNess) {
            return null;
    }

    public Bitmap makeEffectForImage(final String pathImage, final GPUImageFilter effectCache, final GPUImageFilter brightNess) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(pathImage, options);

        return makeEffectForImage(bitmap, effectCache, brightNess);

    }

    public void makeEffectForImage(final String pathImage, GPUImageFilter currentEffect,GPUImageFilter brightNess, OnEffectHelper onEffectHelper) {

    }


    public static interface OnEffectHelper {

        public void onEffectStart();

        public void onEffectSuccess(Bitmap bitmap);

        public void onEffectError(String msg);

    }

    public interface EffectsImageCallBack {
        public void onSuccess();
    }

}
