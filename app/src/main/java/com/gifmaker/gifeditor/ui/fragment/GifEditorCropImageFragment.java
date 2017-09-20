package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorCropImageFragment extends BaseFragment implements View.OnClickListener{

    private View rootView;
    private LinearLayout linearStyleRatio, linearStyleRotate;
    private ImageView buttonRatio;
    private ImageView buttonHorizontal,buttonVertical,buttonRotate,buttonReset;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_editor_crop_image, null, false);
        initIdView();
        initActionView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorCropImageFragment.class.toString());
    }

    private void initIdView() {
        buttonRatio = (ImageView) rootView.findViewById(R.id.buttonRatio);
        linearStyleRatio = (LinearLayout) rootView.findViewById(R.id.linearStyleRatio);
        linearStyleRotate = (LinearLayout) rootView.findViewById(R.id.linearStyleRotate);
        buttonHorizontal = (ImageView) rootView.findViewById(R.id.buttonHorizontal);
        buttonVertical = (ImageView) rootView.findViewById(R.id.buttonVertical);
        buttonRotate = (ImageView) rootView.findViewById(R.id.buttonRotate);
        buttonReset = (ImageView) rootView.findViewById(R.id.buttonReset);
    }

    private void initActionView() {
        buttonRatio.setOnClickListener(this);
        buttonHorizontal.setOnClickListener(this);
        buttonVertical.setOnClickListener(this);
        buttonRotate.setOnClickListener(this);
        buttonReset.setOnClickListener(this);
    }
    private void buttonRatioClick() {
        if(linearStyleRatio.getVisibility()==View.VISIBLE){
            linearStyleRatio.setVisibility(View.GONE);
            linearStyleRotate.setVisibility(View.VISIBLE);
        }else{
            linearStyleRatio.setVisibility(View.VISIBLE);
            linearStyleRotate.setVisibility(View.GONE);
        }
    }

    private void buttonHorizontalClick(){}
    private void buttonVerticalClick(){}
    private void buttonRotateClick(){}
    private void buttonResetlClick(){}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRatio:
                buttonRatioClick();
                break;
            case R.id.buttonHorizontal:
                buttonHorizontalClick();
                break;
            case R.id.buttonVertical:
                buttonVerticalClick();
                break;
            case R.id.buttonRotate:
                buttonRotateClick();
                break;
            case R.id.buttonReset:
                buttonResetlClick();
                break;
            default:break;
        }
    }
}
