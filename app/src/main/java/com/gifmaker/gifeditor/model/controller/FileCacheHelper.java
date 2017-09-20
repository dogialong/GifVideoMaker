package com.gifmaker.gifeditor.model.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.gifmaker.gifeditor.utils.BitMapUtils;
import com.gifmaker.gifeditor.utils.ConfigApp;
import com.gifmaker.gifeditor.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.out;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class FileCacheHelper {

    private Context mContext;
    private File mDirCache;

    public static boolean SAVEIMAGEFAILED = false;
    public static boolean SAVEIMAGESUCCES = true;

    public FileCacheHelper(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        valiDateDir();
    }

    private void valiDateDir() {
        if (mDirCache == null || !mDirCache.exists()) {
            File rootDir = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                rootDir = Environment.getExternalStorageDirectory();
            }

            if (rootDir == null || !rootDir.exists()) {
                rootDir = mContext.getExternalCacheDir();
            }

            if (rootDir == null || !rootDir.exists())
                rootDir = mContext.getCacheDir();

            mDirCache = new File(rootDir, ConfigApp.DIR_CACHE);

            FileUtils.createDir(mDirCache);
        }
    }

    public static void saveBitMaptoFile(final Context context, final int indexFrame, final int indexListFrameImage, final int indexBitmapFrameImage,
                                         Bitmap bitmapImage,  Bitmap allBitMapAddToGif,
                                        final String pathFileImage, final int widthImageView, final int heightImageView, final GifHelper.CopyImageToOutput copyImageToOutput) {

        try {
            File file = new File(pathFileImage);
            Bitmap bitmapImageSave = null;
            if (allBitMapAddToGif != null && bitmapImage != null) {
                bitmapImageSave = BitMapUtils.overlayBitaptoBitmapFrame(context, indexListFrameImage, indexBitmapFrameImage, bitmapImage, allBitMapAddToGif, widthImageView, heightImageView);
                bitmapImageSave.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(file));
            } else if (bitmapImage != null) {
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            }
            bitmapImageSave.recycle();
            bitmapImage.recycle();
            copyImageToOutput.onSuccess(indexFrame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromFile(String pathFile) {

        File image = new File(pathFile);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        return bitmap;
    }

    public File getDirCache() {
        valiDateDir();
        return mDirCache;
    }

    public File getFileByName(String name) {
        File f = new File(getDirCache(), name);
        return f;
    }


    public File getDirFileOrCreateDir(String name) {
        File f = getFileByName(name);
        FileUtils.createDir(f);
        return f;
    }

    public File getFileOrCreateFile(String name) {

        File f = getFileByName(name);
        try {
            FileUtils.createFile(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    public ArrayList<String> getAllPathInFolder(String path) {
        ArrayList<String> arrString = new ArrayList<>();
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            arrString.add(path + "/" + files[i].getName());
        }
        return arrString;
    }


}
