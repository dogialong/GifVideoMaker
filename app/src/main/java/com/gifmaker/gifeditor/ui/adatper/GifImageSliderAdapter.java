package com.gifmaker.gifeditor.ui.adatper;

import android.support.v4.view.PagerAdapter;
import android.view.View;

public class GifImageSliderAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

//    private final static String TAG = GifImageSliderAdapter.class.getSimpleName();
//    private ArrayList<String> images;
//    private LayoutInflater inflater;
//    private Context context;
//    private EffectPreseter effectPreseter;
//    private Filter brightnessImageCurrent;
//    public GifImageSliderAdapter(Context context, ArrayList<String> images, EffectPreseter effectPreseter,
//                                 ArrayList<Filter> listEffect,Filter brightnessImageCurrent) {
//        this.context = context;
//        this.images=images;
//        this.effectPreseter = effectPreseter;
//        inflater = LayoutInflater.from(context);
//        this.brightnessImageCurrent = brightnessImageCurrent;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//     //   container.removeView((View) object);
////        ImageView imageView = (ImageView) ((View) object).findViewById(R.id.image);
////        Drawable drawable = imageView.getDrawable();
////        if (drawable instanceof BitmapDrawable) {
////            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
////            Bitmap bitmap = bitmapDrawable.getBitmap();
////            bitmap.recycle();
////        }
//    }
//
//    @Override
//    public int getCount() {
//
//        int count = images!=null ? images.size() : 0;
//        Logger.d(TAG,"count slide image effect:"+count);
//       return count;
//    }
//    @Override
//    public Object instantiateItem(ViewGroup view, final int position) {
//        Logger.d(TAG, "getView :"+position);
//        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
//        final ImageView myImage = (ImageView) myImageLayout
//                .findViewById(R.id.image);
////        Picasso.with(context).load("file://" + images.get(position)).skipMemoryCache().noFade().into(myImage);
//
//        ManagerThead.getInstance().runOtherThead(new Runnable() {
//            @Override
//            public void run() {
//                effectPreseter.makeEffectForImage(new ImageViewEffectCallBack() {
//                    @Override
//                    public void displayImage(final  Bitmap bitmap) {
//
//                        ManagerThead.getInstance().runUiThead(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(bitmap!=null) {
//                                    Logger.d(TAG,"bitmap is effect ok.................");
//                                    myImage.setImageBitmap(bitmap);
//                                }
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void displayError(String msg) {
//                        Log.d(TAG, "displayError: " + msg);
//                    }
//
//                    @Override
//                    public void displayLoading() {
//
//                    }
//                }, images.get(position),brightnessImageCurrent);
//            }
//        });
//
//        view.addView(myImageLayout);
//
//        return myImageLayout;
//    }
//
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//    public void updateList (ArrayList<String> images,ArrayList<Filter> listEffectsImage) {
//        this.images.clear();
//        this.images.addAll(images);
//        notifyDataSetChanged();
//    }

}