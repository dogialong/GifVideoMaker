package com.gifmaker.gifeditor.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by PhungVanQuang on 8/5/2017.
 */

public class MemoryUtils {

    public static void releaseMemory(ImageView imageView ){
        try {
            if (imageView != null) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                if (bitmapDrawable.getBitmap() !=null && !bitmapDrawable.getBitmap().isRecycled()) {
                    bitmapDrawable.getBitmap().recycle();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void releaseMemoryByGlide(Context context){
        try {
            Glide.get(context).clearMemory();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
