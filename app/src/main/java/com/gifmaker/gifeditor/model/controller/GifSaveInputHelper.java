package com.gifmaker.gifeditor.model.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gifmaker.gifeditor.utils.BitMapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by PhungVanQuang on 8/24/2017.
 */

public class GifSaveInputHelper {

    private Bitmap bitmapAllAddToGif;
    private int indexListFrameImage;
    private int indexFrame;
    private int widthImageView;
    private int heightImageView;
    private GifHelper.CopyImageToOutput copyImageToOutput;
    private Context mContext;

    public GifSaveInputHelper(Context mContext, Bitmap bitmapAllAddToGif, int indexListFrameImage, int indexFrame, int widthImageView, int heightImageView, GifHelper.CopyImageToOutput copyImageToOutput) {
        this.mContext = mContext;
        this.bitmapAllAddToGif = bitmapAllAddToGif;
        this.indexListFrameImage = indexListFrameImage;
        this.indexFrame = indexFrame;
        this.widthImageView = widthImageView;
        this.heightImageView = heightImageView;
        this.copyImageToOutput = copyImageToOutput;

    }

    public void saveImageNoEffect(final String pathInputImage, final int indexImageInGif, final String pathFileOutputImage) {
        ManagerThead.getInstance().runOtherThead(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmapInputImage = BitmapFactory.decodeFile(pathInputImage);
                    File file = new File(pathFileOutputImage);
                    Bitmap bitmapImageSave = null;
                    if (bitmapAllAddToGif != null && bitmapInputImage != null) {
                        bitmapImageSave = BitMapUtils.overlayBitaptoBitmapFrame(mContext, indexListFrameImage, indexFrame, bitmapInputImage, bitmapAllAddToGif, widthImageView, heightImageView);
                        bitmapImageSave.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(file));
                    } else if (bitmapInputImage != null) {
                        bitmapInputImage.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                    }
                    bitmapImageSave.recycle();
                    bitmapInputImage.recycle();
                    copyImageToOutput.onSuccess(indexImageInGif);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void saveImageWithEffect(final GPUImageFilter gpuImageFilter, final String pathInputImage, final int indexImageInGif, final String pathFileOutputImage) {
        ManagerThead.getInstance().runOtherThead(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmapInputImage = BitmapFactory.decodeFile(pathInputImage);
                final GPUImage.CreatFilterImage creatFilterImage = new GPUImage.CreatFilterImage() {
                    @Override
                    public void onSucces(Bitmap bitmap) {
                        try {
                            File file = new File(pathFileOutputImage);
                            Bitmap bitmapImageSave = null;
                            if (bitmapAllAddToGif != null && bitmap != null) {
                                bitmapImageSave = BitMapUtils.overlayBitaptoBitmapFrame(mContext, indexListFrameImage, indexFrame, bitmap, bitmapAllAddToGif, widthImageView, heightImageView);
                                bitmapImageSave.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(file));

                            } else if (bitmap != null) {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                            }
                            if (bitmap != null)
                                bitmap.recycle();
                            if (bitmapImageSave != null)
                                bitmapImageSave.recycle();
                            copyImageToOutput.onSuccess(indexImageInGif);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                };
                GPUImage.getBitmapForFilter(bitmapInputImage, gpuImageFilter, creatFilterImage);
            }
        });
    }
}
