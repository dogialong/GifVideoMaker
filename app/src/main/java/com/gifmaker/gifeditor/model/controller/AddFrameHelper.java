package com.gifmaker.gifeditor.model.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.gifmaker.gifeditor.model.InsertFrame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.gifmaker.gifeditor.utils.StringUtils.loadJSONFromAsset;

/**
 * Created by PhungVanQuang on 8/9/2017.
 */

public class AddFrameHelper {

    public static Bitmap insertFrameType(Context context, int widthImage, int heightImage, int positionListFrame, int positionFrame) {
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        ArrayList<InsertFrame> listFrameCoordinates;
        Bitmap bitmapFrame;
        Bitmap frame = null;

        int resId = context.getResources().getIdentifier("frame" + positionListFrame + positionFrame, "drawable", context.getPackageName());
        bitmapFrame = BitmapFactory.decodeResource(context.getResources(), resId);
        if (positionFrame != 0 || positionListFrame != 0) {
            listFrameCoordinates = getListFrame(context, "frame" + positionListFrame + positionFrame);
            for (InsertFrame frameCut : listFrameCoordinates) {
                bitmapArrayList.add(cutFrame(bitmapFrame, frameCut));
            }


            switch (getStyleFrame(context, "frame" + positionListFrame + positionFrame)) {
                case 1:
                    frame = insertFrameStyle1(widthImage, heightImage, bitmapArrayList, listFrameCoordinates);
                    break;
                case 2:
                    frame = insertFrameStyle2(widthImage, heightImage, bitmapArrayList, listFrameCoordinates);
                    break;
                case 3:
                    frame = insertFrameStyle3(widthImage, heightImage, bitmapArrayList, listFrameCoordinates);
                    break;
                case 4:
                    frame = insertFrameStyle4(widthImage, heightImage, bitmapArrayList, listFrameCoordinates);
                    break;
                case 5:
                    frame = insertFrameStyle5(widthImage, heightImage, bitmapArrayList, listFrameCoordinates);
                    break;
                case 6:
                    frame = insertFrameStyle6(widthImage, heightImage, bitmapArrayList, listFrameCoordinates);
                    break;
                case 7:
                    frame = insertFrameStyle7(widthImage, heightImage, bitmapArrayList, listFrameCoordinates);
                    break;
                case 8:
                    frame = insertFrameStyle8(widthImage, heightImage, bitmapArrayList, listFrameCoordinates);
                    break;
            }
        }
        if (frame != null) {
            if (bitmapFrame != null && !bitmapFrame.isRecycled()) {
                bitmapFrame.recycle();
            }
        }


        if (bitmapArrayList != null) {
            for (Bitmap bitmap : bitmapArrayList) {
                if (bitmap != null)
                    bitmap.recycle();
            }
        }

        return frame != null ? frame : bitmapFrame;
    }


    private static Bitmap cutFrame(Bitmap bmpFrame, InsertFrame frameCut) {
        Bitmap bmOverlay = null;
        if (bmpFrame != null) {
            bmOverlay = Bitmap.createBitmap(bmpFrame, frameCut.getTop(), frameCut.getLeft(), frameCut.getWidth(), frameCut.getHeight());
        }
        return bmOverlay;
    }

    private static ArrayList<InsertFrame> getListFrame(Context context, String nameJson) {
        ArrayList<InsertFrame> listFrameCoordinates = new ArrayList<>();
        String data = "";
        data = loadJSONFromAsset(context, "frameJson/" + nameJson + ".json");
        try {
            String frame;
            JSONObject jsonObjectAnh = new JSONObject(data);
            if (jsonObjectAnh.has("1")) {
                frame = jsonObjectAnh.getString("1");
                listFrameCoordinates.add(getItemFramCut(frame));
            }
            if (jsonObjectAnh.has("2")) {
                frame = jsonObjectAnh.getString("2");
                listFrameCoordinates.add(getItemFramCut(frame));
            }
            if (jsonObjectAnh.has("3")) {
                frame = jsonObjectAnh.getString("3");
                listFrameCoordinates.add(getItemFramCut(frame));
            }
            if (jsonObjectAnh.has("4")) {
                frame = jsonObjectAnh.getString("4");
                listFrameCoordinates.add(getItemFramCut(frame));
            }
            if (jsonObjectAnh.has("12")) {
                frame = jsonObjectAnh.getString("12");
                listFrameCoordinates.add(getItemFramCut(frame));
            }
            if (jsonObjectAnh.has("13")) {
                frame = jsonObjectAnh.getString("13");
                listFrameCoordinates.add(getItemFramCut(frame));
            }
            if (jsonObjectAnh.has("24")) {
                frame = jsonObjectAnh.getString("24");
                listFrameCoordinates.add(getItemFramCut(frame));
            }
            if (jsonObjectAnh.has("34")) {
                frame = jsonObjectAnh.getString("34");
                listFrameCoordinates.add(getItemFramCut(frame));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listFrameCoordinates;
    }

    private static int getStyleFrame(Context context, String nameJson) {
        int styleFrame = 0;
        String data = loadJSONFromAsset(context, "frameJson/" + nameJson + ".json");
        try {
            JSONObject jsonObjectAnh = new JSONObject(data);
            if (jsonObjectAnh.has("0")) {
                styleFrame = Integer.valueOf(jsonObjectAnh.getString("0"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return styleFrame;
    }

    private static Bitmap insertFrameStyle1(int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame> frameCuts) {
        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
        if (bmOverlay != null) {
            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            Bitmap frame = null;
            Bitmap frame1 = null;
            int widthFrame;
            int heightFrame;
            if (widthImage > heightImage) {
                if (heightImage * frameCuts.get(0).getHeight() / 600 < frameCuts.get(0).getHeight()) {
                    heightFrame = heightImage * frameCuts.get(0).getHeight() / 600;
                    widthFrame = heightImage * frameCuts.get(0).getWidth() / 600;
                } else {
                    heightFrame = frameCuts.get(0).getHeight();
                    widthFrame = frameCuts.get(0).getWidth();
                }
            } else {
                if (widthImage * frameCuts.get(0).getWidth() / 600 < frameCuts.get(0).getWidth()) {
                    heightFrame = widthImage * frameCuts.get(0).getHeight() / 600;
                    widthFrame = widthImage * frameCuts.get(0).getWidth() / 600;
                } else {
                    heightFrame = frameCuts.get(0).getHeight();
                    widthFrame = frameCuts.get(0).getWidth();
                }
            }
            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(0), widthFrame, heightFrame, true);
            if (frame != null)
                canvas.drawBitmap(frame, 0, 0, paint);
            if (widthImage > heightImage) {
                if (heightImage * frameCuts.get(1).getHeight() / 600 < frameCuts.get(1).getHeight()) {
                    heightFrame = heightImage * frameCuts.get(1).getHeight() / 600;
                    widthFrame = heightImage * frameCuts.get(1).getWidth() / 600;
                } else {
                    heightFrame = frameCuts.get(1).getHeight();
                    widthFrame = frameCuts.get(1).getWidth();
                }
            } else {
                if (widthImage * frameCuts.get(1).getWidth() / 600 < frameCuts.get(1).getWidth()) {
                    heightFrame = widthImage * frameCuts.get(1).getHeight() / 600;
                    widthFrame = widthImage * frameCuts.get(1).getWidth() / 600;
                } else {
                    heightFrame = frameCuts.get(1).getHeight();
                    widthFrame = frameCuts.get(1).getWidth();
                }
            }

            frame1 = Bitmap.createScaledBitmap(bitmapsFrame.get(1), widthFrame, heightFrame, true);
            if (frame1 != null)
                canvas.drawBitmap(frame1, widthImage - widthFrame, heightImage - heightFrame, paint);

            if (frame1 != null)
                frame1.recycle();
            if (frame != null)
                frame.recycle();
            return bmOverlay;
        } else return null;
    }

    private static Bitmap insertFrameStyle2(int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame> frameCuts) {
        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
        if (bmOverlay != null) {

            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            Bitmap frame1 = null;
            Bitmap frame2 = null;
            Bitmap frame3 = null;
            Bitmap frame4 = null;
            Bitmap frame5 = null;

            if (heightImage > widthImage) {
                frame1 = Bitmap.createScaledBitmap(bitmapsFrame.get(0), widthImage, widthImage * frameCuts.get(0).getHeight() / 600, true);
                if (frame1 != null)
                    canvas.drawBitmap(frame1, 0, 0, paint);
                if (frame2 != null)
                    frame2 = Bitmap.createScaledBitmap(bitmapsFrame.get(1), widthImage, widthImage * frameCuts.get(0).getHeight() / 600, true);
                canvas.drawBitmap(frame2, 0, heightImage - widthImage * frameCuts.get(1).getHeight() / 600, paint);
                int framecenter = (heightImage - widthImage * (frameCuts.get(0).getHeight() + frameCuts.get(1).getHeight()) / 600) / frameCuts.get(2).getHeight();
                if (framecenter < 1) {
                    frame3 = Bitmap.createScaledBitmap(bitmapsFrame.get(2), widthImage, heightImage - widthImage * (frameCuts.get(0).getHeight() + frameCuts.get(1).getHeight()) / 600, true);
                    if (frame3 != null)
                        canvas.drawBitmap(frame3, 0, widthImage * frameCuts.get(0).getHeight() / 600, paint);
                } else {
                    for (int count = 0; count < framecenter; count++) {
                        frame4 = Bitmap.createScaledBitmap(bitmapsFrame.get(2), widthImage, widthImage * frameCuts.get(2).getHeight() / 600, true);
                        if (frame4 != null)
                            canvas.drawBitmap(frame4, 0, widthImage * frameCuts.get(0).getHeight() / 600 + count * widthImage * frameCuts.get(2).getHeight() / 600, paint);
                    }
                    frame5 = Bitmap.createScaledBitmap(bitmapsFrame.get(2), widthImage, heightImage - widthImage * (frameCuts.get(0).getHeight() + frameCuts.get(1).getHeight()) / 600 - framecenter * widthImage * frameCuts.get(2).getHeight() / 600 + 1, true);
                    if (frame5 != null)
                        canvas.drawBitmap(frame5, 0, widthImage * frameCuts.get(0).getHeight() / 600 + framecenter * widthImage * frameCuts.get(2).getHeight() / 600, paint);
                }
            } else {
                frame1 = Bitmap.createScaledBitmap(bitmapsFrame.get(0), widthImage, heightImage * frameCuts.get(0).getHeight() / 600 + 1, true);
                if (frame1 != null)
                    canvas.drawBitmap(frame1, 0, 0, paint);
                frame2 = Bitmap.createScaledBitmap(bitmapsFrame.get(1), widthImage, heightImage * frameCuts.get(1).getHeight() / 600 + 1, true);
                if (frame2 != null)
                    canvas.drawBitmap(frame2, 0, heightImage * (frameCuts.get(0).getHeight() + frameCuts.get(2).getHeight()) / 600, paint);
                if (frame3 != null)
                    frame3 = Bitmap.createScaledBitmap(bitmapsFrame.get(2), widthImage, heightImage * frameCuts.get(2).getHeight() / 600 + 1, true);
                canvas.drawBitmap(frame3, 0, heightImage * frameCuts.get(0).getHeight() / 600, paint);
            }
            if (frame1 != null)
                frame1.recycle();
            if (frame2 != null)
                frame2.recycle();
            if (frame3 != null)
                frame3.recycle();
            if (frame4 != null)
                frame4.recycle();
            if (frame5 != null)
                frame5.recycle();
            return bmOverlay;
        } else return null;
    }

    private static Bitmap insertFrameStyle3(int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame> frameCuts) {
        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
        if (bmOverlay != null) {
            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            Bitmap frame = Bitmap.createScaledBitmap(bitmapsFrame.get(0), widthImage, heightImage, true);
            canvas.drawBitmap(frame, 0, 0, paint);
            if (frame != null)
                frame.recycle();
            return bmOverlay;
        } else return null;
    }

    private static Bitmap insertFrameStyle4(int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame> frameCuts) {
        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
        if (bmOverlay != null) {

            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            int styleImage;
            if (heightImage > widthImage) {
                styleImage = widthImage;
            } else {
                styleImage = heightImage;
            }

            if (styleImage > 600) {
                styleImage = 600;
            }

            Bitmap frame1 = null;
            Bitmap frame2 = null;
            Bitmap frame3 = null;
            Bitmap frame4 = null;
            frame1 = Bitmap.createScaledBitmap(bitmapsFrame.get(0), styleImage * frameCuts.get(0).getWidth() / 600, styleImage * frameCuts.get(0).getHeight() / 600, true);
            if (frame1 != null)
                canvas.drawBitmap(frame1, 0, 0, paint);

            frame2 = Bitmap.createScaledBitmap(bitmapsFrame.get(1), styleImage * frameCuts.get(1).getWidth() / 600, styleImage * frameCuts.get(1).getHeight() / 600, true);
            if (frame2 != null)
                canvas.drawBitmap(frame2, widthImage - styleImage * frameCuts.get(1).getWidth() / 600, 0, paint);

            frame3 = Bitmap.createScaledBitmap(bitmapsFrame.get(2), styleImage * frameCuts.get(2).getWidth() / 600, styleImage * frameCuts.get(2).getHeight() / 600, true);
            if (frame3 != null)
                canvas.drawBitmap(frame3, 0, heightImage - styleImage * frameCuts.get(2).getHeight() / 600, paint);

            frame4 = Bitmap.createScaledBitmap(bitmapsFrame.get(3), styleImage * frameCuts.get(3).getWidth() / 600, styleImage * frameCuts.get(3).getHeight() / 600, true);
            if (frame4 != null)
                canvas.drawBitmap(frame4, widthImage - styleImage * frameCuts.get(3).getWidth() / 600, heightImage - styleImage * frameCuts.get(3).getHeight() / 600, paint);
            if (frame1 != null)
                frame1.recycle();
            if (frame2 != null)
                frame2.recycle();
            if (frame3 != null)
                frame3.recycle();
            if (frame4 != null)
                frame4.recycle();
            return bmOverlay;
        } else return null;
    }

    private static Bitmap insertFrameStyle5(int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame> frameCuts) {
        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
        if (bmOverlay != null) {

            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            int styleImage;
            if (heightImage > widthImage) {
                styleImage = widthImage;
            } else {
                styleImage = heightImage;
            }

            if (styleImage > 600) {
                styleImage = 600;
            }

            Bitmap frame1 = null;
            Bitmap frame2 = null;
            frame1 = Bitmap.createScaledBitmap(bitmapsFrame.get(0), widthImage, styleImage * frameCuts.get(0).getHeight() / 600, true);
            if (frame1 != null)
                canvas.drawBitmap(frame1, 0, 0, paint);

            frame2 = Bitmap.createScaledBitmap(bitmapsFrame.get(1), widthImage, styleImage * frameCuts.get(1).getHeight() / 600, true);
            if (frame2 != null)
                canvas.drawBitmap(frame2, 0, heightImage - styleImage * frameCuts.get(1).getHeight() / 600, paint);
            if (frame1 != null)
                frame1.recycle();
            if (frame2 != null)
                frame2.recycle();
            return bmOverlay;
        } else return null;
    }

    //    private static Bitmap insertFrameStyle7 (int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame>  frameCuts) {
//        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
//        if (bmOverlay != null) {
//
//            Canvas canvas = new Canvas(bmOverlay);
//            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
//            int styleImage;
//            if (heightImage > widthImage) {
//                styleImage = widthImage;
//            } else {
//                styleImage = heightImage;
//            }
//
//            if (styleImage > 600) {
//                styleImage = 600;
//            }
//
//            Bitmap frame = null;
//            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(0), styleImage * frameCuts.get(0).getWidth() / 600, styleImage * frameCuts.get(0).getHeight() / 600, true);
//            if (frame != null)
//                canvas.drawBitmap(frame, 0, 0, paint);
//
//            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(1), styleImage * frameCuts.get(1).getWidth() / 600, styleImage * frameCuts.get(1).getHeight() / 600, true);
//            if (frame != null)
//                canvas.drawBitmap(frame, widthImage - styleImage * frameCuts.get(1).getWidth()/600,0, paint);
//
//            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(2),styleImage * frameCuts.get(2).getWidth() / 600,styleImage * frameCuts.get(2).getHeight() / 600, true);
//            if (frame != null) {
//                canvas.drawBitmap(frame,0,heightImage - frameCuts.get(2).getHeight()/600,paint);
//            }
//            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(3),styleImage * frameCuts.get(3).getWidth() / 600,styleImage * frameCuts.get(3).getHeight() / 600,true);
//            if (frame != null) {
//                canvas.drawBitmap(frame,widthImage - frameCuts.get(3).getWidth(),heightImage - frameCuts.get(3).getHeight()/600,paint);
//            }
//            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(4),styleImage * frameCuts.get(4).getWidth() / 600,styleImage * frameCuts.get(4).getHeight() / 600,true);
//            if (frame != null) {
//                canvas.drawBitmap(frame,widthImage - frameCuts.get(4).getWidth(),0,paint);
//            }
//            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(5),styleImage * frameCuts.get(5).getWidth() / 600,styleImage * frameCuts.get(5).getHeight() / 600,true);
//            if (frame != null) {
//                canvas.drawBitmap(frame,0,heightImage - frameCuts.get(5).getHeight()/600,paint);
//            }
//
//            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(6),styleImage * frameCuts.get(6).getWidth() / 600,styleImage * frameCuts.get(6).getHeight() / 600,true);
//            if (frame != null) {
//                canvas.drawBitmap(frame,widthImage - frameCuts.get(7).getWidth(),heightImage - frameCuts.get(6).getHeight()/600,paint);
//            }
//
//            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(7),styleImage * frameCuts.get(7).getWidth() / 600,styleImage * frameCuts.get(7).getHeight() / 600,true);
//            if (frame != null) {
//                canvas.drawBitmap(frame,widthImage - frameCuts.get(7).getWidth(),heightImage - frameCuts.get(7).getHeight()/600,paint);
//            }
//
//
//            if (frame != null)
//                frame.recycle();
//            return bmOverlay;
//        } else return null;
//    }
    private static Bitmap insertFrameStyle7(int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame> frameCuts) {
        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
        if (bmOverlay != null) {

            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            int styleImage;
            if (heightImage > widthImage) {
                styleImage = widthImage;
            } else {
                styleImage = heightImage;
            }

            if (styleImage <= 600) {
                Bitmap frame = null;
                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(0), styleImage * frameCuts.get(0).getWidth() / 600,
                        styleImage * frameCuts.get(0).getHeight() / 600, true);
                canvas.drawBitmap(frame, 0, 0, paint);

                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(1), styleImage * frameCuts.get(1).getWidth() / 600,
                        styleImage * frameCuts.get(1).getHeight() / 600, true);
                canvas.drawBitmap(frame, widthImage - styleImage * frameCuts.get(1).getWidth() / 600, 0, paint);
                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(2), styleImage * frameCuts.get(2).getWidth() / 600,
                        styleImage * frameCuts.get(2).getHeight() / 600, true);
                canvas.drawBitmap(frame, 0, heightImage - styleImage * frameCuts.get(2).getHeight() / 600, paint);
                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(3), styleImage * frameCuts.get(3).getWidth() / 600,
                        styleImage * frameCuts.get(3).getHeight() / 600, true);
                canvas.drawBitmap(frame, widthImage - styleImage * frameCuts.get(3).getWidth() / 600
                        , heightImage - styleImage * frameCuts.get(3).getHeight() / 600, paint);
                int widthInsertTopBot = widthImage - 2 * styleImage * frameCuts.get(0).getWidth() / 600;
                if (widthInsertTopBot <= bitmapsFrame.get(4).getWidth()) {
                    InsertFrame insertFrame = new InsertFrame(0, 0, widthInsertTopBot, styleImage * frameCuts.get(4).getHeight() / 600);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(4), insertFrame), styleImage * frameCuts.get(0).getWidth() / 600, 0, paint);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(4), insertFrame), styleImage * frameCuts.get(0).getWidth() / 600,
                            heightImage - styleImage * frameCuts.get(4).getHeight() / 600, paint);
                    Log.e("Linh", styleImage * frameCuts.get(0).getWidth() / 600 + " s" +
                            (heightImage - styleImage * frameCuts.get(4).getHeight() / 600) + " " + styleImage * frameCuts.get(4).getHeight() / 600);
                    Log.e("image", heightImage + " " + styleImage * frameCuts.get(4).getHeight() / 600);
                } else {
                    Log.e("linh", "ss33");
                    int countInsertTopbot = widthInsertTopBot / bitmapsFrame.get(4).getWidth();
                    Log.e("zz", widthInsertTopBot + "  " + bitmapsFrame.get(4).getWidth());
                    Log.e("zz", countInsertTopbot + " ");
                    for (int i = 0; i < countInsertTopbot; i++) {
                        frame = Bitmap.createScaledBitmap(bitmapsFrame.get(4), frameCuts.get(4).getWidth(),
                                styleImage * frameCuts.get(4).getHeight() / 600, true);
                        if (frame != null)
                            canvas.drawBitmap(frame, styleImage * frameCuts.get(0).getWidth() / 600, 0, paint);
                        frame = Bitmap.createScaledBitmap(bitmapsFrame.get(7), frameCuts.get(7).getWidth(),
                                styleImage * frameCuts.get(7).getHeight() / 600, true);
                        Log.e("linhzz", styleImage * frameCuts.get(7).getHeight() / 600 + "");
                        if (frame != null)
                            canvas.drawBitmap(frame, styleImage * frameCuts.get(3).getWidth() / 600, heightImage - styleImage * frameCuts.get(7).getHeight() / 600, paint);
                        Log.e("log", styleImage * frameCuts.get(3).getWidth() / 600 + " ");
                        Log.e("log", heightImage - styleImage * frameCuts.get(7).getHeight() / 600 + " ");
                    }
                    int widthinserttopbot = widthInsertTopBot - countInsertTopbot * bitmapsFrame.get(4).getWidth();
                    InsertFrame insertFrametopbot = new InsertFrame(0, 0, widthinserttopbot, styleImage * frameCuts.get(7).getHeight() / 600);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(4), insertFrametopbot), styleImage * frameCuts.get(0).getWidth() / 600 + countInsertTopbot * bitmapsFrame.get(4).getWidth(), 0, paint);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(7), insertFrametopbot), styleImage * frameCuts.get(0).getWidth() / 600 + countInsertTopbot * bitmapsFrame.get(7).getWidth()
                            , heightImage - styleImage * frameCuts.get(7).getHeight() / 600, paint);
                    Log.e("log", heightImage - styleImage * frameCuts.get(7).getHeight() / 600 + " ");
                    Log.e("log", cutFrame(bitmapsFrame.get(7), insertFrametopbot).getWidth() + " ");

                }
                int heightInsertlefright = heightImage - 2 * styleImage * frameCuts.get(0).getHeight() / 600;
                if (heightInsertlefright <= bitmapsFrame.get(5).getHeight()) {
                    InsertFrame insertlefright = new InsertFrame(0, 0, styleImage * frameCuts.get(5).getWidth() / 600, heightInsertlefright);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(5), insertlefright), 0,
                            styleImage * frameCuts.get(0).getHeight() / 600, paint);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(5), insertlefright), widthImage - styleImage * frameCuts.get(6).getWidth() / 600,
                            styleImage * frameCuts.get(0).getHeight() / 600, paint);
                } else {
                    int countHeight = heightInsertlefright / bitmapsFrame.get(5).getHeight();
                    for (int i = 0; i < countHeight; i++) {
                        frame = Bitmap.createScaledBitmap(bitmapsFrame.get(5),
                                styleImage * frameCuts.get(5).getWidth() / 600, frameCuts.get(5).getHeight(), true);
                        canvas.drawBitmap(frame, 0, styleImage * frameCuts.get(0).getHeight() / 600 + i * frameCuts.get(5).getHeight(), paint);
                        frame = Bitmap.createScaledBitmap(bitmapsFrame.get(6),
                                styleImage * frameCuts.get(6).getWidth() / 600, frameCuts.get(6).getHeight(), true);
                        canvas.drawBitmap(frame, widthImage - styleImage * frameCuts.get(6).getWidth() / 600,
                                styleImage * frameCuts.get(1).getHeight() / 600 + i * frameCuts.get(6).getHeight(), paint);
                    }
                    int inserframeleftright = heightInsertlefright - countHeight * bitmapsFrame.get(5).getHeight();
                    InsertFrame insertFrameleftright = new InsertFrame(0, 0, styleImage * frameCuts.get(5).getWidth() / 600, inserframeleftright);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(5), insertFrameleftright), 0, styleImage *
                            frameCuts.get(0).getWidth() / 600 + countHeight * bitmapsFrame.get(5).getHeight(), paint);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(6), insertFrameleftright), widthImage - styleImage * frameCuts.get(6).getWidth() / 600, styleImage *
                            frameCuts.get(0).getWidth() / 600 + countHeight * bitmapsFrame.get(6).getHeight(), paint);
                }

            } else {
                styleImage = 600;
                Bitmap frame = null;
                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(0), frameCuts.get(0).getWidth(),
                        frameCuts.get(0).getHeight(), true);
                canvas.drawBitmap(frame, 0, 0, paint);
                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(1), frameCuts.get(1).getWidth(),
                        frameCuts.get(1).getHeight(), true);
                canvas.drawBitmap(frame, widthImage - frameCuts.get(1).getWidth(), 0, paint);
                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(2), frameCuts.get(2).getWidth(),
                        frameCuts.get(2).getHeight(), true);
                canvas.drawBitmap(frame, 0, heightImage - frameCuts.get(2).getHeight(), paint);
                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(3), frameCuts.get(3).getWidth(),
                        frameCuts.get(3).getHeight(), true);
                canvas.drawBitmap(frame, widthImage - frameCuts.get(3).getWidth(), heightImage - frameCuts.get(3).getHeight(), paint);
                int withinserttopbot = widthImage - 2 * frameCuts.get(0).getWidth();
                if (withinserttopbot < frameCuts.get(4).getWidth()) {
                    int witdhCut = frameCuts.get(4).getWidth() - withinserttopbot;
                    InsertFrame insertFrameCut = new InsertFrame(0, 0, witdhCut, frameCuts.get(4).getHeight());
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(4), insertFrameCut), frameCuts.get(0).getWidth(), 0, paint);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(7), insertFrameCut), frameCuts.get(2).getWidth(),
                            heightImage - frameCuts.get(7).getHeight(), paint);


                } else {
                    int countCut = withinserttopbot / frameCuts.get(4).getWidth();
                    for (int i = 0; i < countCut; i++) {
                        frame = Bitmap.createScaledBitmap(bitmapsFrame.get(4), frameCuts.get(4).getWidth(), frameCuts.get(4).getHeight(), true);
                        if (frame != null)
                            canvas.drawBitmap(frame, frameCuts.get(0).getWidth() + i * frameCuts.get(4).getWidth(), 0, paint);
                        frame = Bitmap.createScaledBitmap(bitmapsFrame.get(7), frameCuts.get(7).getWidth(),
                                frameCuts.get(7).getHeight(), true);
                        if (frame != null)
                            canvas.drawBitmap(frame, frameCuts.get(2).getWidth() + i * frameCuts.get(7).getWidth(), heightImage - frameCuts.get(7).getHeight(), paint);
                    }
                    int widthCut = withinserttopbot - countCut * frameCuts.get(4).getWidth();
                    InsertFrame insertFrameCut = new InsertFrame(0, 0, widthCut, frameCuts.get(4).getHeight());
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(4), insertFrameCut), frameCuts.get(0).getWidth() + countCut * frameCuts.get(4).getWidth(), 0, paint);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(7), insertFrameCut), frameCuts.get(2).getWidth() + countCut * frameCuts.get(4).getWidth(),
                            heightImage - frameCuts.get(7).getHeight(), paint);

                }
                int heightCutLefRight = heightImage - 2 * frameCuts.get(0).getHeight();
                if (heightCutLefRight <= frameCuts.get(5).getHeight()) {
                    int heightCutFrame = frameCuts.get(5).getHeight() - heightCutLefRight;
                    InsertFrame insertFrameCutHeight = new InsertFrame(0, 0, frameCuts.get(5).getWidth(), heightCutFrame);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(5), insertFrameCutHeight), 0, frameCuts.get(0).getHeight(), paint);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(6), insertFrameCutHeight), widthImage - frameCuts.get(6).getWidth()
                            , frameCuts.get(0).getHeight(), paint);
                } else {
                    int count = heightCutLefRight / frameCuts.get(5).getHeight();
                    Log.e("ss", frameCuts.get(5).getHeight() + " ");
                    for (int i = 0; i < count; i++) {
                        frame = Bitmap.createScaledBitmap(bitmapsFrame.get(5), frameCuts.get(5).getWidth()
                                , frameCuts.get(5).getHeight(), true);
                        if (frame != null)
                            canvas.drawBitmap(frame, 0, frameCuts.get(0).getHeight() + i * frameCuts.get(5).getHeight(), paint);
                        frame = Bitmap.createScaledBitmap(bitmapsFrame.get(6), frameCuts.get(6).getWidth()
                                , frameCuts.get(6).getHeight(), true);
                        if (frame != null)
                            canvas.drawBitmap(frame, widthImage - frameCuts.get(6).getWidth(), frameCuts.get(1).getHeight() + i * frameCuts.get(6).getHeight(), paint);

                    }
                    int heightCutlefrig = heightCutLefRight - count *
                            frameCuts.get(5).getHeight();
                    InsertFrame insertFrame = new InsertFrame(0, 0, frameCuts.get(5).getWidth(), heightCutlefrig);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(5), insertFrame), 0,
                            frameCuts.get(0).getHeight() + count * frameCuts.get(5).getHeight(), paint);
                    canvas.drawBitmap(cutFrame(bitmapsFrame.get(6), insertFrame), widthImage - frameCuts.get(6).getWidth(),
                            frameCuts.get(0).getHeight() + count * frameCuts.get(6).getHeight(), paint);


                }
                if (frame != null)
                    frame.recycle();
            }
            return bmOverlay;
        } else return null;
    }

    private static Bitmap insertFrameStyle6(int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame> frameCuts) {
        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
        if (bmOverlay != null) {
            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            int styleImage;
            if (heightImage > widthImage) {
                styleImage = widthImage;
            } else {
                styleImage = heightImage;
            }

            if (styleImage <= 600) {
                Bitmap frame = null;
                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(0), styleImage * frameCuts.get(0).getWidth() / 600,
                        styleImage * frameCuts.get(0).getHeight() / 600, true);
                canvas.drawBitmap(frame, 0, 0, paint);

                frame = Bitmap.createScaledBitmap(bitmapsFrame.get(1), styleImage * frameCuts.get(1).getWidth() / 600,
                        styleImage * frameCuts.get(1).getHeight() / 600, true);
                canvas.drawBitmap(frame, widthImage - styleImage * frameCuts.get(1).getWidth() / 600, 0, paint);
            } else {
            Bitmap frame = null;
            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(0), frameCuts.get(0).getWidth(), frameCuts.get(0).getHeight(), true);
            if (frame != null) {
                double count = (double) heightImage / (frameCuts.get(0).getHeight());
                int i = 0;
                for (i = 0; i < count; i++) {
                    frame = Bitmap.createScaledBitmap(bitmapsFrame.get(0), frameCuts.get(0).getWidth(),
                            frameCuts.get(0).getHeight(), true);
                    if (frame != null)
                        canvas.drawBitmap(frame, 0, 0, paint);
                }
                int heightInsertFrame = Math.abs((heightImage - ((int) count) * (frameCuts.get(0).getHeight())));
                InsertFrame insertFrame = new InsertFrame(0, 0, frameCuts.get(0).getWidth(), heightInsertFrame);

                canvas.drawBitmap(cutFrame(bitmapsFrame.get(0), insertFrame), 0, ((int) count) * 600, paint);
            }

            frame = Bitmap.createScaledBitmap(bitmapsFrame.get(1), frameCuts.get(1).getWidth(), frameCuts.get(1).getHeight(), true);
            if (frame != null) {
                double count = (double) heightImage / (frameCuts.get(1).getHeight());
                for (int i = 0; i < count; i++) {
                    frame = Bitmap.createScaledBitmap(bitmapsFrame.get(1), frameCuts.get(1).getWidth(),
                            frameCuts.get(1).getHeight(), true);
                    if (frame != null)
                        canvas.drawBitmap(frame, widthImage - frameCuts.get(1).getWidth(), 0, paint);
                }
                int heightInsertFrame = Math.abs((heightImage - ((int) count) * (frameCuts.get(1).getHeight())));
                InsertFrame insertFrame = new InsertFrame(0, 0, frameCuts.get(1).getWidth(), heightInsertFrame);
                canvas.drawBitmap(cutFrame(bitmapsFrame.get(1), insertFrame), widthImage - frameCuts.get(1).getWidth(), ((int) count) * 600, paint);
            }
            if (frame != null)
                frame.recycle();
        }
            return bmOverlay;
        } else return null;
    }


    private static Bitmap insertFrameStyle8(int widthImage, int heightImage, ArrayList<Bitmap> bitmapsFrame, ArrayList<InsertFrame> frameCuts) {
        Bitmap bmOverlay = Bitmap.createBitmap(widthImage, heightImage, Bitmap.Config.ARGB_8888);
        if (bmOverlay != null) {
            Canvas canvas = new Canvas(bmOverlay);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            Bitmap frame1 = null;
            Bitmap frame2 = null;
            int styleImage;
            if (heightImage > widthImage) {
                styleImage = widthImage;
            } else {
                styleImage = heightImage;
            }

            if (styleImage <= 600) {

                frame1 = Bitmap.createScaledBitmap(bitmapsFrame.get(0), styleImage * frameCuts.get(0).getWidth() / 600,
                        styleImage * frameCuts.get(0).getHeight() / 600, true);
                canvas.drawBitmap(frame1, widthImage - styleImage * frameCuts.get(0).getWidth() / 600, 0, paint);

                frame2 = Bitmap.createScaledBitmap(bitmapsFrame.get(1), styleImage * frameCuts.get(1).getWidth() / 600,
                        styleImage * frameCuts.get(1).getHeight() / 600, true);
                canvas.drawBitmap(frame1, 0, heightImage - styleImage * frameCuts.get(1).getHeight() / 600, paint);
                if (frame1 != null)
                    frame1.recycle();
                if (frame2 != null)
                    frame2.recycle();
            } else {
                frame1 = Bitmap.createScaledBitmap(bitmapsFrame.get(0), frameCuts.get(0).getWidth(), frameCuts.get(0).getHeight(), true);
                canvas.drawBitmap(frame1, widthImage - frameCuts.get(0).getWidth(), 0, paint);
                frame2 = Bitmap.createScaledBitmap(bitmapsFrame.get(1), frameCuts.get(1).getWidth(), frameCuts.get(1).getHeight(), true);
                canvas.drawBitmap(frame2, 0, heightImage - frameCuts.get(1).getHeight(), paint);

                if (frame1 != null)
                    frame1.recycle();
                if (frame2 != null)
                    frame2.recycle();
            }
            return bmOverlay;
        } else return null;
    }


    private static InsertFrame getItemFramCut(String informationFrame) {
        InsertFrame frameCut = new InsertFrame();
        String[] frameCoordinates;
        frameCoordinates = informationFrame.split(",");
        frameCut.setTop(Integer.valueOf(frameCoordinates[0]));
        frameCut.setLeft(Integer.valueOf(frameCoordinates[1]));
        frameCut.setWidth(Integer.valueOf(frameCoordinates[2]));
        frameCut.setHeight(Integer.valueOf(frameCoordinates[3]));
        return frameCut;
    }
}
