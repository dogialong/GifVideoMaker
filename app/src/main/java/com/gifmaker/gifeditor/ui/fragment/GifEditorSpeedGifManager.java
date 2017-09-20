package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;
import com.gifmaker.gifeditor.ui.view.GifEditorSpeedGifCallBack;
import com.gifmaker.gifeditor.ui.view.SliderBar;

import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;

import static com.gifmaker.gifeditor.R.id.seekBarChangeBrightness;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorSpeedGifManager extends BaseFragment {

    private View rootView;
    private SeekBar sliderBarChangeSpeed;
    private GifEditorSpeedGifCallBack speegGifCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_editor_speed_gif, null, false);
        initView(rootView);
        initActionView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorSpeedGifManager.class.toString());
    }

    private void initView(View view) {
        sliderBarChangeSpeed = (SeekBar) view.findViewById(R.id.sliderBarChangeSpeed);
    }

    private void initActionView() {
        sliderBarChangeSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() == 1000) {
                    speegGifCallBack.initFpsImageGif(0.002f);
                } else {
                    speegGifCallBack.initFpsImageGif(((float) (1000-seekBar.getProgress()) /500));
                }
            }
        });
    }

    public void initSpeedImageCallBack(GifEditorSpeedGifCallBack speegGifCallBack) {
        this.speegGifCallBack = speegGifCallBack;
    }


}
