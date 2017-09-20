package com.gifmaker.gifeditor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gifmaker.gifeditor.model.controller.AddFrameHelper;

/**
 * Created by PhungVanQuang on 8/4/2017.
 */

public class BitMapUtils {

    public static final int SIZEIMAGETYPEVERTICAL = 0;
    public static final int SIZEIMAGETYPEHOZIRONTAL = 1;

    public static int calculateInSampleSize(int widthInput, int heightInput, int reqWidthOutput, int reqHeightOutput) {
        // Raw height and width of image
        int inSampleSize = 1;

        if (heightInput > reqHeightOutput || widthInput > reqWidthOutput) {
            final int halfHeight = heightInput / 2;
            final int halfWidth = widthInput / 2;
            while ((halfHeight / inSampleSize) >= reqHeightOutput
                    && (halfWidth / inSampleSize) >= reqWidthOutput) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledFileImageToBitmap(String pathImage, int widthOutputBitMap, int heightOutputBitMap) {
        Bitmap outputBitMap = null;
        Bitmap inputBitMap = null;
        int isSampleSize = 0;
        int withFileInput;
        int heightFileInput;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathImage, options);

        withFileInput = options.outWidth;
        heightFileInput = options.outHeight;
        isSampleSize = calculateInSampleSize(withFileInput, heightFileInput, widthOutputBitMap, heightOutputBitMap);

        options.inSampleSize = isSampleSize;
        options.inJustDecodeBounds = false;
        options.inMutable = true;

        inputBitMap = BitmapFactory.decodeFile(pathImage, options);
        outputBitMap = Bitmap.createScaledBitmap(inputBitMap, withFileInput / isSampleSize, heightFileInput / isSampleSize, true);

        return outputBitMap;
    }

    public static Bitmap decodeSampledImageToBitmap(Bitmap bitMapInput, int widthOutputBitMap, int heightOutputBitMap) {
        Bitmap outputBitMap = null;

        int isSampleSize = 0;
        int withFileInput;
        int heightFileInput;

        withFileInput = bitMapInput.getWidth();
        heightFileInput = bitMapInput.getHeight();
        isSampleSize = calculateInSampleSize(withFileInput, heightFileInput, widthOutputBitMap, heightOutputBitMap);

        outputBitMap = Bitmap.createScaledBitmap(bitMapInput, withFileInput / isSampleSize, heightFileInput / isSampleSize, true);

        return outputBitMap;
    }

    public static int checkTypeSizeBitMap(String pathImage) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathImage, options);
        if (options.outHeight > options.outWidth) {
            return SIZEIMAGETYPEVERTICAL;
        } else {
            return SIZEIMAGETYPEHOZIRONTAL;
        }
    }


    public static Bitmap overlayBitaptoBitmapFrame(Context context, int indexListFrameImage, int indexBitmapFrameImage, Bitmap imageToGif, Bitmap allBitMapAddToGif, int widthImgView, int heightImgView) {
        double ratioImageSelectshow = (double) widthImgView / heightImgView;
        if (imageToGif != null) {
            double ratioImagePathShow = (double) imageToGif.getWidth() / imageToGif.getHeight();
            int width = imageToGif.getWidth();
            int height = imageToGif.getHeight();
            if (ratioImagePathShow < ratioImageSelectshow) {
                width = imageToGif.getHeight() * imageToGif.getWidth() / imageToGif.getHeight();
                height = imageToGif.getHeight();
            } else if (ratioImagePathShow > ratioImageSelectshow) {
                width = imageToGif.getWidth();
                height = imageToGif.getWidth() * heightImgView / widthImgView;
            }
            float left = (float) (width - imageToGif.getWidth()) / 2;
            float top = (float) (height - imageToGif.getHeight()) / 2;

            Bitmap bmOverlay = Bitmap.createBitmap(width, height, imageToGif.getConfig());




            Bitmap itemAddtoGif = Bitmap.createScaledBitmap(allBitMapAddToGif, width, height, false);
            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

            Bitmap frameAddToGif = AddFrameHelper.insertFrameType(context, imageToGif.getWidth(), imageToGif.getHeight(), indexListFrameImage, indexBitmapFrameImage);
            canvas.drawBitmap(imageToGif, left, top, paint);
            canvas.drawBitmap(itemAddtoGif, 0, 0, paint);
            if (frameAddToGif != null) {
                canvas.drawBitmap(frameAddToGif, left, top, paint);
            }

            if (frameAddToGif != null) {
                frameAddToGif.recycle();
                frameAddToGif = null;
            }

            if (itemAddtoGif != null) {
                itemAddtoGif.recycle();
                itemAddtoGif = null;
            }


            Runtime.getRuntime().gc();
            return bmOverlay;
        } else return null;
    }

}
