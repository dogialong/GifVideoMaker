package com.gifmaker.gifeditor.model.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.utils.Develop;
import com.gifmaker.gifeditor.utils.FileUtils;
import com.gifmaker.gifeditor.utils.Logger;
import com.gifmaker.gifeditor.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by mr.logic on 7/27/2017.
 */

public class GifHelper {

    private final static String TAG = GifHelper.class.getSimpleName();
    private final static String PREFIX_NAME_GIF = "imageInput";
    private final static String DIR_INPUT_GIF = "input";
    public final static String DIR_OUTPUT_GIF = "output";
    public final static String DIR_OUTPUT_TEMP_GIF = "temp";
    public final static String EXTENTION_GIF = "gif";
    private Context mContext;
    private FileCacheHelper mFileCacheHelper;
    private String mPathInputMedia;
    private int mDuration;
    private int mVideoHeight;
    private int mVideoWidth;
    private int mRatioScale;
    private int mTotalThumb;
    public int widthImageView;

    public GifHelper(Context context, String pathInputMedia) {
        this.mContext = context;
        this.mPathInputMedia = pathInputMedia;
        init();
    }

    public GifHelper(Context context, String pathInputMedia, int totalImage) {
        this.mContext = context;
        this.mPathInputMedia = pathInputMedia;
        mFileCacheHelper = new FileCacheHelper(mContext);
    }

    public GifHelper(Context context) {
        this(context, null);
    }


    private void init() {
        mFileCacheHelper = new FileCacheHelper(mContext);
        if (!TextUtils.isEmpty(mPathInputMedia)) {
            reviceInforInputVideo();
            caculatorNumberFrameThumbGif();
        }
    }

    public void loadAutoThumbGif(int position, final OnAutoExtractFrameFromVideo onAutoExtractFrameFromVideo) {
        final File fOutput = getFileOutputThumbGif(position);
        if (FileUtils.getLenghtFile(fOutput) > 0) {
            onAutoExtractFrameFromVideo.onSuccess(fOutput);
        } else {
            int seekTime = getAutoTimeSeekFrame(position);
            extractOneFrameFromVideo(seekTime, mRatioScale, fOutput.getPath(), new FFmpegHelper.OnExecuteCommandFFmpeg() {
                @Override
                public void onError(String msg) {
                    Logger.d(TAG, "onError:" + msg);
                }

                @Override
                public void onSuccess(String message) {
                    Logger.d(TAG, "onSuccess:" + message);
                }

                @Override
                public void onProgress(String message) {
                    Logger.d(TAG, "onProgress:" + message);
                }

                @Override
                public void onFailure(String message) {
                    Logger.d(TAG, "onFailure:" + message);
                }

                @Override
                public void onStart() {
                    Logger.d(TAG, "onStart:");
                }

                @Override
                public void onFinish() {
                    Logger.d(TAG, "onFinish:");
                    onAutoExtractFrameFromVideo.onSuccess(fOutput);
                }
            });
        }
    }

    public int getAutoTotalThumbGif() {
        return mTotalThumb;
    }

    public int getAutoTimeSeekFrame(int position) {
        int seekTime = getDurationFileVideo() * position / mTotalThumb;
        return seekTime;
    }

    public File getFileOutputThumbGif(int position) {
        int seekTime = getAutoTimeSeekFrame(position);
        String namefile = mPathInputMedia.hashCode() + "" + seekTime + StringUtils.format4Digit(position) + ".png";
        final File fOutput = new File(getDirInputImageGif(), namefile);
        try {
            FileUtils.createFile(fOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fOutput;
    }

    public void caculatorNumberFrameThumbGif() {

        Display display = ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();

        int heightImageView;

        if (getVideoHeight() > getVideoWidth()) {
            heightImageView = (int) mContext.getResources().getDimension(R.dimen.layout_height_60dp);
        } else {
            heightImageView = (int) mContext.getResources().getDimension(R.dimen.layout_height_40dp);
        }

        mTotalThumb = 15;
        widthImageView = width / (mTotalThumb + 1);

        caculatorRatioThumb(widthImageView);

    }

    private void caculatorRatioThumb(int widthImageView) {
        mRatioScale = 1;
        while (getVideoWidth() > (widthImageView * mRatioScale)) {
            mRatioScale = mRatioScale * 2;
        }
        mRatioScale = mRatioScale / 2;
    }


    private void reviceInforInputVideo() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mPathInputMedia);

        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mDuration = Integer.parseInt(time) / 1000;

        Bitmap bmp = retriever.getFrameAtTime();
        mVideoHeight = bmp.getHeight();
        mVideoWidth = bmp.getWidth();

    }

    public int getDurationFileVideo() {
        return mDuration;
    }


    public int getVideoHeight() {
        return mVideoHeight;
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public void extractFrame() {

    }

    //thư mục input tạo file gif
    public File getDirInputImageGif() {
        return mFileCacheHelper.getDirFileOrCreateDir(DIR_INPUT_GIF);
    }

    //thư mục đầu ra của file gif
    public File getDirOutputImageGif() {
        return mFileCacheHelper.getDirFileOrCreateDir(DIR_OUTPUT_GIF);
    }

    public File getDirOutputTempImageGif() {
        return mFileCacheHelper.getDirFileOrCreateDir(DIR_OUTPUT_TEMP_GIF);
    }

    //file đầu vào tạo file Gif
    private File getFileInputImageGif() {

        return new File(getDirInputImageGif(), PREFIX_NAME_GIF);
    }

    //file gif
    private File getFileOutputImageGif() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss.mm.HH.DD-MM-YY");
        simpleDateFormat.format(calendar.getTime());
        String name = simpleDateFormat.toString();

        return new File(getDirOutputImageGif(), name);
    }


    public void extractOneFrameFromVideo(final int seekSecondTime, final int ratioSpect, final String pathOutPut, final FFmpegHelper.OnExecuteCommandFFmpeg onExecuteCommandFFmpeg) {
        final String[] command = FFmpegHelper.makeCommandExtractOneFrameVideo(mPathInputMedia, pathOutPut, mVideoWidth, ratioSpect, seekSecondTime);
        ManagerThead.getInstance().runOtherThead(new Runnable() {
            @Override
            public void run() {
                FFmpegHelper.executeCommand(mContext, command, onExecuteCommandFFmpeg);
            }
        });

    }

    public ArrayList<String> getNameOfAllFrame(String path, int startTime) {
        ArrayList<String> arrListPath = new ArrayList<>();
        String strNumber = "";
        for (int i = 0; i < FFmpegHelper.TOTAL_IMAGE_EXTRACT_ALL_FRAME; i++) {
            strNumber = new DecimalFormat("0000").format(startTime + i) + ".png";
            arrListPath.add(path + strNumber);
        }
        return arrListPath;
    }

    public ArrayList<String> getNameOfAllFrameFromGif(String path, int totalFrame) {
        ArrayList<String> arrListPath = new ArrayList<>();
        String strNumber = "";
        for (int i = 1; i < totalFrame; i++) {
            strNumber = new DecimalFormat("0000").format(i) + ".png";
            arrListPath.add(path + strNumber);
        }
        return arrListPath;
    }

    public void extractAllFrameFromVideo(final int seektime, final int duration, final OnExtractAllFrame onExtractAllFrame) {
        File fileOutputDir = new File(getDirInputImageGif(), new File(mPathInputMedia).getName().hashCode() + "");
        FileUtils.createDir(fileOutputDir);
        String prefixName = genPrefixNameFile();
        final File fileOutput = new File(fileOutputDir, prefixName);
        ManagerThead.getInstance().runOtherThead(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < FFmpegHelper.TOTAL_IMAGE_EXTRACT_ALL_FRAME; i++) {
                    String[] command = FFmpegHelper.makeCommandExtractAllFrameVideo(mPathInputMedia, fileOutput.getAbsolutePath() + String.format("%04d", i) + ".png", seektime + duration * i / FFmpegHelper.TOTAL_IMAGE_EXTRACT_ALL_FRAME, 0);
                    final int finalI = i;
                    FFmpegHelper.executeCommand(mContext, command, new FFmpegHelper.OnExecuteCommandFFmpeg() {
                        @Override
                        public void onError(String msg) {
                            Log.e(Develop.TAG, "onError: " + msg);
                            if (onExtractAllFrame != null)
                                onExtractAllFrame.onError(msg);
                        }

                        @Override
                        public void onSuccess(String message) {
                            onExtractAllFrame.onSuccess(fileOutput.getAbsolutePath());
                        }

                        @Override
                        public void onProgress(String message) {
                            Log.e(Develop.TAG, "onProgress: " + finalI);
                            onExtractAllFrame.onProgess(finalI);
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.e(Develop.TAG, "onFailure: " + message);
                            if (onExtractAllFrame != null)
                                onExtractAllFrame.onError(message);
                        }

                        @Override
                        public void onStart() {
                            if (onExtractAllFrame != null)
                                onExtractAllFrame.onStart();
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                }
            }
        });
    }

//    public void extractAllFrameFromVideo(final int seektime, final int endtime, final OnExtractAllFrame onExtractAllFrame) {
//        File fileOutputDir = new File(getDirInputImageGif(), new File(mPathInputMedia).getName().hashCode() + "");
//        FileUtils.createDir(fileOutputDir);
//        String prefixName = genPrefixNameFile();
//        final File fileOutput = new File(fileOutputDir, prefixName);
//
//        final MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
//        mRetriever.setDataSource(mPathInputMedia);
//        ManagerThead.getInstance().runOtherThead(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 20; i++) {
//                    FileUtils.saveBitmapToFile(mRetriever.getFrameAtTime(seektime + i * (endtime - seektime) / 20, MediaMetadataRetriever.OPTION_CLOSEST_SYNC), fileOutput.getAbsolutePath() + String.format("%04d", i) + ".png");
//
//                    Logger.e(Develop.TAG, fileOutput.getAbsolutePath() + String.format("%04d", i) + ".png");
//
//                    onExtractAllFrame.onProgess(i);
//                    Logger.e(Develop.TAG, "i" + i);
//                    if (i == 19)
//                        onExtractAllFrame.onSuccess(fileOutput.getAbsolutePath());
//                }
//            }
//        });
//
//
//    }

    public void extractAllFrameFromGif(final OnExtractAllFrame onExtractAllFrame) {
        String prefixName = genPrefixNameFile();
        final File fileOutput = new File(getDirInputImageGif(), prefixName);
        ManagerThead.getInstance().runOtherThead(new Runnable() {
            @Override
            public void run() {
                String[] command = FFmpegHelper.makeCommandExtractAllFrameGif(mPathInputMedia, fileOutput.getAbsolutePath() + "%04d.png", 0);
                FFmpegHelper.executeCommand(mContext, command, new FFmpegHelper.OnExecuteCommandFFmpeg() {
                    @Override
                    public void onError(String msg) {
                        Log.e(Develop.TAG, "onError: " + msg);
                        if (onExtractAllFrame != null)
                            onExtractAllFrame.onError(msg);
                    }

                    @Override
                    public void onSuccess(String message) {
                        Log.e(Develop.TAG, "onSuccess: " + message);
                        onExtractAllFrame.onSuccess(fileOutput.getAbsolutePath());
                    }

                    @Override
                    public void onProgress(String message) {
                        if (message.substring(0, 6).equals("frame=")) {
                            String progressFrame = message.split("fps=")[0].replace(" ", "");
                            int indexFrameProgress = Integer.valueOf(progressFrame.split("=")[1]);
                            onExtractAllFrame.onProgess(indexFrameProgress);
                        }
                        Log.e(Develop.TAG, "onProgress: " + message);
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e(Develop.TAG, "onFailure: " + message);
                        if (onExtractAllFrame != null)
                            onExtractAllFrame.onError(message);
                    }

                    @Override
                    public void onStart() {
                        Log.e(Develop.TAG, "onStart: ");
                        if (onExtractAllFrame != null)
                            onExtractAllFrame.onStart();
                    }

                    @Override
                    public void onFinish() {
                        Log.e(Develop.TAG, "onFinish: ");
                    }
                });
            }
        });
    }


    public String copyImageSelectedToInput(final ArrayList<Image> images, final CopyImageToOutput copyImageToOutput) {
        final String namefile = getDirInputImageGif().getAbsolutePath() + File.separator + genPrefixNameFile();
        ManagerThead.getInstance().runOtherThead(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.size(); i++) {
                    FileUtils.copyImageToImagePNG(images.get(i).getPath(), namefile + String.format("%04d", i) + ".jpeg");
                    copyImageToOutput.onSuccess(i);
                }
            }

        });
        return namefile;
    }

    public void copyImageFromVideoToInput(final ArrayList<Image> images, final String namefile, final CopyImageToOutput copyImageToOutput) throws IOException {
        ManagerThead.getInstance().runOtherThead(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.size(); i++) {
                    FileUtils.copyImageToImagePNG(images.get(i).getPath(), namefile + String.format("%04d", i) + ".png");
                    copyImageToOutput.onSuccess(i);
                }
            }

        });
    }

    private String genPrefixNameFile() {
        return System.currentTimeMillis() + "_makegif";
    }

    public void makeGifFrom(final String mPathInputImage, String nameFileGif, final double fps, final OnMakeGif onMakeGif,String mimeTypeImage) {
        try {
            File fileOutput = new File(getDirOutputTempImageGif(), nameFileGif + ".gif");
            FileUtils.createFile(fileOutput);
            final String pathOut = fileOutput.getAbsolutePath();
            String[] command = FFmpegHelper.makeCommandMakeGifFromImage(mPathInputImage, pathOut, 0, fps,mimeTypeImage);
            FFmpegHelper.executeCommand(mContext, command, new FFmpegHelper.OnExecuteCommandFFmpeg() {
                @Override
                public void onError(String msg) {
                    if (onMakeGif != null)
                        onMakeGif.onError(msg);
                }

                @Override
                public void onSuccess(String message) {
                    onMakeGif.onSuccess(pathOut);

                }

                @Override
                public void onProgress(String message) {

                }

                @Override
                public void onFailure(String message) {
                    Logger.e(Develop.TAG, message);
                    if (onMakeGif != null)
                        onMakeGif.onError(message);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {
                    if (onMakeGif != null)
                        onMakeGif.onSuccess(pathOut);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            if (onMakeGif != null)
                onMakeGif.onError(e.toString());
        }
    }


    public interface OnAutoExtractFrameFromVideo {

        public void onError();

        public void onSuccess(File output);


    }

    public interface OnExtractAllFrame {
        public void onError(String error);

        public void onSuccess(String msg);

        public void onProgess(int percent);

        public void onStart();
    }

    public interface OnMakeGif extends OnExtractAllFrame {

    }

    public interface CopyImageToOutput {
        public void onSuccess(int positionCopy);
    }

}
