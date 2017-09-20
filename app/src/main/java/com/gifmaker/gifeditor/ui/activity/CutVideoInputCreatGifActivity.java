package com.gifmaker.gifeditor.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.model.controller.GifHelper;
import com.gifmaker.gifeditor.ui.fragment.BasePreviewThumbGifFragment;
import com.gifmaker.gifeditor.ui.fragment.CutPreviewThumbGifFragment;
import com.gifmaker.gifeditor.ui.fragment.PreviewVideoFragment;
import com.gifmaker.gifeditor.ui.view.TimeDurationListener;
import com.gifmaker.gifeditor.utils.FileUtils;
import com.gifmaker.gifeditor.utils.Logger;

public class CutVideoInputCreatGifActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = CutVideoInputCreatGifActivity.class.getSimpleName();
    private String pathSelectedVideo = "";
    private ImageView imgApply, imgBack;
    private String pathVideo;
    private BasePreviewThumbGifFragment basePreviewThumbGifFragment;
    public TimeDurationListener timeDurationListener;
    private int timeStart , timeEnd;
    private GifHelper gifHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_video_input_creat_gif);
        initView();
        initValueToActivity();
    }

    private void initValueToActivity() {

        pathSelectedVideo = getIntent().getStringExtra("Uri");
        Logger.d(TAG, "pathSelectedVideo:" + pathSelectedVideo);
        pathVideo = FileUtils.getPathFile(getApplicationContext(), Uri.parse(pathSelectedVideo));
        goToFragment(R.id.frameReviewData, PreviewVideoFragment.getInstance(pathVideo));
        goToFragment(R.id.frameThumbData, CutPreviewThumbGifFragment.getInstance(pathVideo));
        gifHelper = new GifHelper(getApplicationContext(),pathVideo);
        timeDurationListener = new TimeDurationListener() {
            @Override
            public void getMaxValue(int maxvalue) {
                timeEnd = maxvalue/1000;
            }

            @Override
            public void getMinValue(int minValue) {
                if (minValue == 0 ) {
                    timeStart = 0;
                } else {
                    timeStart = minValue/1000;
                }
            }
        };
        basePreviewThumbGifFragment = new BasePreviewThumbGifFragment();
        basePreviewThumbGifFragment.initTimeDurationCallback(timeDurationListener);
    }


    private void initView() {
        imgApply = (ImageView) findViewById(R.id.buttonApply);
        imgBack = (ImageView) findViewById(R.id.buttonCancel);
        imgBack.setOnClickListener(this);
        imgApply.setOnClickListener(this);

    }

    private void goToFragment(int idFrameLayout, Fragment fragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit, R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        ft.replace(idFrameLayout, fragment);
        ft.commit();
    }

    private void handlerApply() {
        if (timeEnd == 0) {
            if (gifHelper != null) {
                timeEnd =  gifHelper.getDurationFileVideo();
            }
        }
        Intent intent = new Intent(CutVideoInputCreatGifActivity.this, GifEditorActivity.class);
        intent.putExtra("input", "video");
        intent.putExtra("startVideo", timeStart);
        intent.putExtra("endVideo", timeEnd);
        intent.putExtra("Uri", pathVideo);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonApply:
                handlerApply();
                break;
            case R.id.buttonCancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        basePreviewThumbGifFragment.unregisterTimeDurationCallback();
    }
}
