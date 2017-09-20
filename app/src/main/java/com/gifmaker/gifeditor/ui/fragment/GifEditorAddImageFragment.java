package com.gifmaker.gifeditor.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorAddImageFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private RelativeLayout chooseCamera, chooseLibrary;
    private final static int PICK_IMAGE = 199;
    private final static int OPEN_CAMERA = 200;
    private sendData sendData;
    public interface sendData {
        public void dataImage(Uri uri);
        public void addImage(Bitmap bitmap);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendData = (sendData) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_editor_add_image, null, false);
        initIdView();
        initActionView();
        return rootView;
    }

    private void initIdView() {
        chooseCamera = (RelativeLayout) rootView.findViewById(R.id.choose_from_camera);
        chooseLibrary = (RelativeLayout) rootView.findViewById(R.id.choose_from_library);


    }

    private void initActionView() {
        chooseCamera.setOnClickListener(this);
        chooseLibrary.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorAddImageFragment.class.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_from_camera:
                chooseImageFromCamera();
                break;
            case R.id.choose_from_library:
                chooseImageFromLibrary();
                break;
        }
    }
    private void chooseImageFromCamera(){


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager =getActivity().getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(intent, 0);
        intent.setPackage(listCam.get(0).activityInfo.packageName);
        startActivityForResult(intent,OPEN_CAMERA);


    }
    private void chooseImageFromLibrary(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                sendData.dataImage(uri);
            }
        }else if(requestCode == OPEN_CAMERA){
            if(resultCode == RESULT_OK){
                if(data!=null) {
                    Log.e("data", data.toString());
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    sendData.addImage(photo);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
   
}
