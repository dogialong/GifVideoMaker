package com.gifmaker.gifeditor.model.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gifmaker.gifeditor.utils.BitMapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

import static android.R.attr.bitmap;
import static com.gifmaker.gifeditor.R.id.imageViewPreviewGif;

/**
 * Created by PhungVanQuang on 8/24/2017.
 */

public class GifSaveHelper {

    private final static String TAG = GifSaveHelper.class.getSimpleName();
    private Context mContext;
    private ExecutorService executorService;
    private WeakHashMap<String,WeakReference<Bitmap>> poolBitmaps;

    private void releaeMemory(){
        ArrayList<String> keyset = (ArrayList<String>) poolBitmaps.keySet();
        for (String key : keyset){
            WeakReference<Bitmap> data = poolBitmaps.get(key);
            if(data!=null)
                data.get().recycle();
        }
    }


    private void putBitmapToPool(String path,Bitmap bitmap){
        poolBitmaps.put(path,new WeakReference<Bitmap>(bitmap));
    }

    private void putBitmapToPool(String path,WeakReference<Bitmap> bitmap){
        poolBitmaps.put(path,bitmap);
    }



    public GifSaveHelper(Context context){
        this.mContext = context;

        iniṭ̣();

    }

    private void iniṭ̣(){
        if(executorService==null || executorService.isShutdown() || executorService.isTerminated()){
            executorService = Executors.newSingleThreadExecutor();
        }
    }



    public void saveBitMaptoFile(GPUImageFilter gpuImageFilter, Bitmap bitmapAllAddToGif, GifHelper.CopyImageToOutput copyImageToOutput, int indexFrame, int indexListFrameImage, int indexBitmapFrameImage,
                                 String pathFileImage, int widthImageView, int heightImageView){
//        final GPUImageFilter gpuImageFilter = imageViewPreviewGif.getFilter();
        final GPUImage.CreatFilterImage creatFilterImage = new GPUImage.CreatFilterImage() {
            @Override
            public void onSucces(Bitmap bitmap) {
           //     makeGif(bitmap, finalIndexImageInGif, bitmapAllAddToGif);
            }
        };

//        ManagerThead.getInstance().runOtherThead(new Runnable() {
//            @Override
//            public void run() {
//                GPUImage.getBitmapForFilter(bitmapDecodeImage, gpuImageFilter, creatFilterImage);
//            }
//        });
    }


    public void saveBitMaptoFile(String pathInput, Bitmap bitmapAllAddToGif, GifHelper.CopyImageToOutput copyImageToOutput, int indexFrame, int indexListFrameImage, int indexBitmapFrameImage,
                                 String pathFileImage, int widthImageView, int heightImageView) {


        executorService.execute(new RunnableProcessImage(mContext,pathInput,bitmapAllAddToGif,copyImageToOutput,indexFrame,indexListFrameImage,indexBitmapFrameImage,pathFileImage,widthImageView,heightImageView));

    }



    private static class RunnableProcessImage implements Runnable{

        private GifHelper.CopyImageToOutput mCopyImageToOutput;

        private int indexFrame;
        private int indexListFrameImage;
        private int indexBitmapFrameImage;
        private String pathFileImage;
        private int widthImageView;
        private int heightImageView;
        private String pathInupt;

        private Context mContext;

        private WeakReference<Bitmap>  bitmapImagew;
        private WeakReference<Bitmap> bitmapItemw;


        public RunnableProcessImage(Context context,String pathInput, Bitmap bitmapAllAddToGif, GifHelper.CopyImageToOutput copyImageToOutput, int indexFrame, int indexListFrameImage, int indexBitmapFrameImage,
                                    String pathFileImage, int widthImageView, int heightImageView){

            this.mContext = context;
            this.pathInupt = pathInput;
            this.mCopyImageToOutput = copyImageToOutput;
            this.indexFrame = indexFrame;
            this.indexListFrameImage = indexListFrameImage;
            this.indexBitmapFrameImage = indexBitmapFrameImage;
            this.pathFileImage = pathFileImage;
            this.widthImageView = widthImageView;
            this.heightImageView = heightImageView;
            bitmapItemw = new WeakReference<Bitmap>(bitmapAllAddToGif);
        }



        @Override
        public void run() {

            try {

                Bitmap bitmapImage = BitmapFactory.decodeFile(pathInupt);
                bitmapImagew = new WeakReference<Bitmap>(bitmapImage);

                File file = new File(pathFileImage);
                Bitmap bitmapImageSave = null;
                if (bitmapImagew.get() != null && bitmapItemw.get() != null) {
                    Log.d(TAG,"no effect..........111111111");
                    bitmapImageSave = BitMapUtils.overlayBitaptoBitmapFrame(mContext, indexListFrameImage, indexBitmapFrameImage, bitmapImagew.get(), bitmapItemw.get(), widthImageView, heightImageView);
                    bitmapImageSave.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(file));
                } else if (bitmapImagew.get() != null ) {

                    Log.d(TAG,"no effect..........");
                    bitmapImagew.get().compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                }

                if(bitmapImageSave!=null)
                    bitmapImageSave.recycle();

                if (bitmapImagew.get()  != null){
                    bitmapImagew.get().recycle();
                }


                mCopyImageToOutput.onSuccess(indexFrame);
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }


}
