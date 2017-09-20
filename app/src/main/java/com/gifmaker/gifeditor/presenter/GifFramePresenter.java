package com.gifmaker.gifeditor.presenter;

import android.widget.ImageView;

import com.gifmaker.gifeditor.model.controller.GifHelper;
import com.gifmaker.gifeditor.ui.view.PreviewThumbGifViewUser;

import java.io.File;

/**
 * Created by mr.logic on 7/27/2017.
 */

public class GifFramePresenter extends  BasePresenter{

    private GifHelper mGifHelper;
    private PreviewThumbGifViewUser mPreviewThumbGifViewUser;


    private String mPathInputMedia;


    public GifFramePresenter(GifHelper gifHelper, PreviewThumbGifViewUser previewThumbGifViewUser){
        this.mGifHelper = gifHelper;
        this.mPreviewThumbGifViewUser = previewThumbGifViewUser;
    }

    public int getTotalThumbGif(){
        return mGifHelper.getAutoTotalThumbGif();
    }

    public void loadAutoThumbGif(final ImageView imageView, int position){

        mGifHelper.loadAutoThumbGif(position, new GifHelper.OnAutoExtractFrameFromVideo() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(File output) {
                if (imageView!=null) {
                    mPreviewThumbGifViewUser.displayGifThumb(imageView,output);
                }
            }
        });
    }



}
