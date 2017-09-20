package com.ahmedadeltito.photoeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by Ahmed Adel on 02/06/2017.
 */

public class PhotoEditorSDK implements MultiTouchListener.OnMultiTouchListener {
    int positonview = -1;
    private Context context;
    private RelativeLayout parentView;
    private ImageView imageView;
    private View deleteView;
    private List<View> addedViews, AddedViews;
    private OnPhotoEditorSDKListener onPhotoEditorSDKListener;
    private View addTextRootView;
    private TextView addTextView;
    private ImageView img;

    private PhotoEditorSDK(PhotoEditorSDKBuilder photoEditorSDKBuilder) {
        this.context = photoEditorSDKBuilder.context;
        this.parentView = photoEditorSDKBuilder.parentView;
        this.imageView = photoEditorSDKBuilder.imageView;
        this.deleteView = photoEditorSDKBuilder.deleteView;
        addedViews = new ArrayList<>();
        AddedViews = new ArrayList<>();
    }

    public void addImage(Bitmap desiredImage) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageRootView = inflater.inflate(R.layout.photo_editor_sdk_image_item_list, null);
        ImageView imageView = (ImageView) imageRootView.findViewById(R.id.photo_editor_sdk_image_iv);
        imageView.setImageBitmap(desiredImage);
        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
                parentView,addedViews, this.imageView, onPhotoEditorSDKListener);
        multiTouchListener.setOnMultiTouchListener(this);
        imageRootView.setOnTouchListener(multiTouchListener);
       if(imageView!=null){
           ViewGroup parentViewGroup = (ViewGroup) imageView.getParent();
           if (parentViewGroup != null) {
               parentViewGroup.removeView(imageView);
           }
       }
        parentView.addView(imageView);
        addedViews.add(imageRootView);
        if (onPhotoEditorSDKListener != null)
            onPhotoEditorSDKListener.onAddViewListener(ViewType.IMAGE, addedViews.size());
    }



    public void addText(String text, Typeface typeface, int colorCodeTextView) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addTextRootView = inflater.inflate(R.layout.photo_editor_sdk_text_item_list, null);
         addTextView = (TextView) addTextRootView.findViewById(R.id.photo_editor_sdk_text_tv);
        addTextView.setGravity(Gravity.CENTER);
        addTextView.setText(text);

        if (colorCodeTextView != -1)
            addTextView.setTextColor(colorCodeTextView);
        if(typeface!= null)
            addTextView.setTypeface(typeface);

        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
                parentView,addedViews, this.imageView, onPhotoEditorSDKListener);
        multiTouchListener.setOnMultiTouchListener(this);
        addTextRootView.setOnTouchListener(multiTouchListener);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        parentView.addView(addTextRootView, params);
        addedViews.add(addTextRootView);
        if (onPhotoEditorSDKListener != null)
            onPhotoEditorSDKListener.onAddViewListener(ViewType.TEXT, addedViews.size());

    }

    public void addEmoji(String emojiName, Typeface emojiFont) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View emojiRootView = inflater.inflate(R.layout.photo_editor_sdk_text_item_list, null);
        TextView emojiTextView = (TextView) emojiRootView.findViewById(R.id.photo_editor_sdk_text_tv);
        emojiTextView.setTypeface(emojiFont);
        emojiTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        emojiTextView.setText(convertEmoji(emojiName));
        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
                parentView,addedViews, this.imageView, onPhotoEditorSDKListener);
        multiTouchListener.setOnMultiTouchListener(this);
        emojiRootView.setOnTouchListener(multiTouchListener);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        parentView.addView(emojiRootView, params);
        addedViews.add(emojiRootView);
        if (onPhotoEditorSDKListener != null)
            onPhotoEditorSDKListener.onAddViewListener(ViewType.EMOJI, addedViews.size());
    }

    public void viewUndo() {
        if (addedViews.size() > 0) {
            parentView.removeView(addedViews.remove(addedViews.size() - 1));
            if (onPhotoEditorSDKListener != null)
                onPhotoEditorSDKListener.onRemoveViewListener(addedViews.size());
        }
    }
    public void viewUndoFrame() {
        if(AddedViews.size() == 0){

        }else {
            parentView.removeView(AddedViews.get(0));
            AddedViews.remove(0);
        }
    }
    private void viewUndo(View removedView) {
        if (addedViews.size() > 0) {
            if (addedViews.contains(removedView)) {
                parentView.removeView(removedView);
                addedViews.remove(removedView);
                if (onPhotoEditorSDKListener != null)
                    onPhotoEditorSDKListener.onRemoveViewListener(addedViews.size());
            }
        }
    }

    public String saveImage(String folderName, String imageName) {
        String selectedOutputPath = "";
        if (isSDCARDMounted()) {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);
            // Create a storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("PhotoEditorSDK", "Failed to create directory");
                }
            }
            // Create a media file name
            selectedOutputPath = mediaStorageDir.getPath() + File.separator + imageName;
            Log.d("PhotoEditorSDK", "selected camera path " + selectedOutputPath);
            File file = new File(selectedOutputPath);
            try {
                FileOutputStream out = new FileOutputStream(file);
                if (parentView != null) {
                    parentView.setDrawingCacheEnabled(true);
                    parentView.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 80, out);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return selectedOutputPath;
    }

    public Bitmap getAllAddToGif() {
        Bitmap bm = null;
        if (parentView != null) {
            parentView.setDrawingCacheEnabled(true);
            parentView.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 80, out);
            parentView.buildDrawingCache();
            bm = parentView.getDrawingCache();
        }
        return bm;
    }

    private boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    private String convertEmoji(String emoji) {
        String returnedEmoji = "";
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public void setOnPhotoEditorSDKListener(OnPhotoEditorSDKListener onPhotoEditorSDKListener) {
        this.onPhotoEditorSDKListener = onPhotoEditorSDKListener;
    }

    public void addFrame(Bitmap desiredImage) {
        if (AddedViews.size() > 0) {
            parentView.removeView(AddedViews.remove(0));
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageRootView = inflater.inflate(R.layout.photo_editor_sdk_image_item_list, null);
        ImageView imageView = (ImageView) imageRootView.findViewById(R.id.photo_editor_sdk_image_iv);
        imageView.setImageBitmap(desiredImage);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        parentView.addView(imageRootView, params);
        AddedViews.add(imageRootView);
        if (onPhotoEditorSDKListener != null)
            onPhotoEditorSDKListener.onAddViewListener(ViewType.IMAGE, AddedViews.size());
    }

    @Override
    public void onEditTextClickListener(String text, int colorCode,Typeface typeface,View view,int position) {
        if (addedViews.contains(view)) {
            if(positonview== -1){
                ImageView img2 = (ImageView) addedViews.get(position).findViewById(R.id.img);
                img2.setVisibility(View.VISIBLE);
                addedViews.get(position).setBackgroundResource(R.drawable.bolder_image_photoeditor);
                positonview = position;
                Log.e("position1",position + " " + positonview);

            }
            if(positonview == position){

            }else {
               ImageView img = (ImageView)addedViews.get(positonview).findViewById(R.id.img);
                ImageView img2 = (ImageView) addedViews.get(position).findViewById(R.id.img);
                img.setVisibility(View.GONE);
                img2.setVisibility(View.VISIBLE);
                addedViews.get(position).setBackgroundResource(R.drawable.bolder_image_photoeditor);
                addedViews.get(positonview).setBackgroundResource(0);
                Log.e("position",position + " " + positonview);
                positonview = position;
            }
//            parentView.removeView(view);
//            addedViews.remove(view);
        }
    }
    @Override
    public void onRemoveViewListener(View removedView) {
        viewUndo(removedView);
    }

    public static class PhotoEditorSDKBuilder {

        private Context context;
        private RelativeLayout parentView;
        private ImageView imageView;
        private View deleteView;

        public PhotoEditorSDKBuilder(Context context) {
            this.context = context;
        }

        public PhotoEditorSDKBuilder parentView(RelativeLayout parentView) {
            this.parentView = parentView;
            return this;
        }

        public PhotoEditorSDKBuilder childView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public PhotoEditorSDKBuilder deleteView(View deleteView) {
            this.deleteView = deleteView;
            return this;
        }

        public PhotoEditorSDK buildPhotoEditorSDK() {
            return new PhotoEditorSDK(this);
        }
    }
}
