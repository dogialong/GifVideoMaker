package com.gifmaker.gifeditor.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static com.gifmaker.gifeditor.utils.DeviceUtils.getDataColumn;

/**
 * Created by mr.logic on 7/27/2017.
 */

public class FileUtils {

    public static final String CONTENT = "content";
    public static final String FILE = "file";
    public static final String PRIMARY = "primary";
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";


    public static void copyFileOrDirectory(String pathFileFrom, String pathFileTo) {
        try {
            File src = new File(pathFileFrom);
            File dst = new File(pathFileTo);
            if (src.isDirectory()) {
                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    public static long getLenghtFile(File f) {
        if (f != null && f.exists())
            return f.length();
        return 0;
    }

    public static void createDir(File dir) {
        if (dir != null && !dir.exists())
            dir.mkdirs();
    }

    public static void createFile(File f) throws IOException {
        if (f != null && !f.exists())
            f.createNewFile();
    }

    public static String getPathFile(final Context context, final Uri uri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if (DeviceUtils.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if (PRIMARY.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            } else if (DeviceUtils.isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (DeviceUtils.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if (IMAGE.equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (VIDEO.equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (AUDIO.equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if (FILE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static void deleteFile(String pathFile) {
        File file = new File(pathFile);
        file.delete();
    }

    public static void deleteFileInFolder(String pathFolder) {
        File folder = new File(pathFolder);
        if (folder.exists())
            for (File file : folder.listFiles()) {
                file.delete();
            }
    }

    public static File creatFile(String name, String directory) {
        File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), directory);
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        File output = new File(cacheDir, name);
        return output;
    }

    public static File saveBitmapToFile(Bitmap bmp, String path) {
        OutputStream outStream = null;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            file = new File(path);
        }
        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void copyImageToImagePNG(String nameImageInput, String nameImageOutput) {
        try {
            FileOutputStream out = new FileOutputStream(nameImageOutput);
            Bitmap bmp = BitmapFactory.decodeFile(nameImageInput);
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, out);
            bmp.recycle();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int getScreenWidth(Activity mContext) {
        int screenWidth = 0;
        if (Build.VERSION.SDK_INT >= 13) {
            Display display = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay();
            Point point = new Point();

            display.getSize(point);
            screenWidth = point.x;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            screenWidth = displayMetrics.widthPixels;
        }

        return screenWidth;
    }

    public static int getScreenHeight(Activity mContext) {
        int screenHeight = 0;
        if (Build.VERSION.SDK_INT >= 13) {
            Display display = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            screenHeight = point.y;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            screenHeight = displayMetrics.heightPixels;

        }
        return screenHeight;
    }

    public static ArrayList<String> getListFileOfTypeFromDir(File directory, String typeFile) {
        ArrayList<String> listPathFile = new ArrayList<>();
        for (File file : directory.listFiles()) {
            int i = file.getAbsolutePath().lastIndexOf('.');
            if (i > 0) {
                if (typeFile.equals(file.getAbsolutePath().substring(i + 1))) {
                    listPathFile.add(file.getAbsolutePath());
                }
            }
        }
        return listPathFile;
    }

}
