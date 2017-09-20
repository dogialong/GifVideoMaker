package com.gifmaker.gifeditor.presenter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class ImageLoaderPresenter {

    private final static int WIDTH_THUMB = 48;
    private final static int HEIGHT_THUMB = 120;
    public static void  displayThumbImage(Context context, ImageView imageView, String path,int width){
        if (context != null) {
            Glide.with(context)
                    .load(path)
                    .skipMemoryCache(false)
                    .override(width, HEIGHT_THUMB)
                    .into(imageView);
        }

    }

    public static void  displayImage(Context context, ImageView imageView, String path){
        Glide.with(context)
                .load(path)
                .skipMemoryCache(false)
                .into(imageView);
    }
}
