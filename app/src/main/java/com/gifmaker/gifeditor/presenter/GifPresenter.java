package com.gifmaker.gifeditor.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.gifmaker.gifeditor.model.controller.GifHelper;
import com.gifmaker.gifeditor.ui.view.GifExtractFrameFromVideoViewUser;
import com.gifmaker.gifeditor.ui.view.GifMakeViewUser;

import java.io.IOException;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by mr.logic on 7/27/2017.
 */

public class GifPresenter extends BasePresenter {


    private GifExtractFrameFromVideoViewUser extractFrameFromVideoViewUser;

    private GifHelper mGifHelper;

    private Context context;

    public GifPresenter(Context context, GifHelper mGifHelper, GifExtractFrameFromVideoViewUser extractFrameFromVideoViewUser) {
        this.context = context;
        this.mGifHelper = mGifHelper;
        this.extractFrameFromVideoViewUser = extractFrameFromVideoViewUser;
    }

    public GifPresenter(Context context, GifHelper mGifHelper) {
        this.context = context;
        this.mGifHelper = mGifHelper;
    }

    public GifPresenter(Context context, String pathImage) {
        this.context = context;
        this.mGifHelper = new GifHelper(this.context, pathImage);
    }

    public void makeGif(String mPathInputImage, String nameImageGif, double fps,String mimeTypeImage, final GifMakeViewUser gifMakeViewUser) {
        mGifHelper.makeGifFrom(mPathInputImage, nameImageGif, fps, new GifHelper.OnMakeGif() {
                    @Override
                    public void onError(String error) {
                        gifMakeViewUser.showError(error);
                    }

                    @Override
                    public void onSuccess(String msg) {
                        gifMakeViewUser.showSuccess(msg);
                    }

                    @Override
                    public void onProgess(int index) {
                        gifMakeViewUser.showProgres(index);
                    }

                    @Override
                    public void onStart() {
                        gifMakeViewUser.showLoading();
                    }
                },mimeTypeImage
        );

    }

    public String copyImageSelectedToInput(ArrayList<Image> listImages, GifHelper.CopyImageToOutput copyImageToOutput) {
        String pathInputCollectcImage = "";
        pathInputCollectcImage = mGifHelper.copyImageSelectedToInput(listImages, copyImageToOutput);
        return pathInputCollectcImage;
    }

    public void copyImageFromVideoToInput(ArrayList<Image> listImages, String fileNameAllImagePath, GifHelper.CopyImageToOutput copyImageToOutput) {
        try {
            mGifHelper.copyImageFromVideoToInput(listImages, fileNameAllImagePath, copyImageToOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Image> getListImage(ArrayList<String> arrPath) {
        ArrayList<Image> arrImage = new ArrayList<>();
        for (int i = 0; i < arrPath.size(); i++) {
            arrImage.add(new Image(0, "", arrPath.get(i), false));
        }
        return arrImage;
    }

    // seek : time start, duration : time end - time start
    public void extractAllFrameFromVideo(int seektime, int duration) {

        mGifHelper.extractAllFrameFromVideo(seektime, duration, new GifHelper.OnExtractAllFrame() {
            @Override
            public void onError(String error) {
                extractFrameFromVideoViewUser.showError(error);
            }

            @Override
            public void onSuccess(String msg) {
                extractFrameFromVideoViewUser.showSuccess(msg);
            }

            @Override
            public void onProgess(int index) {
                extractFrameFromVideoViewUser.showProgres(index);
            }

            @Override
            public void onStart() {
                extractFrameFromVideoViewUser.showLoading();
            }
        });
    }

    public void extractAllFrameFromGif() {

        mGifHelper.extractAllFrameFromGif(new GifHelper.OnExtractAllFrame() {
            @Override
            public void onError(String error) {
                extractFrameFromVideoViewUser.showError(error);
            }

            @Override
            public void onSuccess(String msg) {
                extractFrameFromVideoViewUser.showSuccess(msg);
            }

            @Override
            public void onProgess(int index) {
                extractFrameFromVideoViewUser.showProgres(index);
            }

            @Override
            public void onStart() {
                extractFrameFromVideoViewUser.showLoading();
            }
        });
    }

    public ArrayList<String> getPathFromListImage(ArrayList<Image> arrImage) {
        ArrayList<String> arrString = new ArrayList<>();
        for (int i = 0; i < arrImage.size(); i++) {
            arrString.add(arrImage.get(i).getPath());
        }
        return arrString;
    }

    public ArrayList<String> getPathFromVideo(String linkPath, int startTime) {
        ArrayList<String> arrPath = mGifHelper.getNameOfAllFrame(linkPath, startTime);
        return arrPath;
    }

    public ArrayList<String> getPathFromGif(String linkPath, int totalFrame) {
        ArrayList<String> arrPath = mGifHelper.getNameOfAllFrameFromGif(linkPath, totalFrame);
        return arrPath;
    }

    public int getDurationVideo() {
        return mGifHelper.getDurationFileVideo();
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, image.getIntrinsicWidth() / 2, image.getIntrinsicHeight() / 2, false);
        BitmapDrawable bitmapResult = new BitmapDrawable(context.getResources(), bitmapResized);
        if (bitmapResized != b) {
            b.recycle();
            b = null;
        }
        return bitmapResult;
    }

    public void removeFrameInListFrame(ArrayList<Image> arr, int position, RecyclerView.Adapter adapter) {
        arr.remove(position);
        adapter.notifyDataSetChanged();
    }
}
