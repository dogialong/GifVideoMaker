package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;
import com.gifmaker.gifeditor.ui.view.GifEditorBrightnessImageCallBack;

import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorBrightnessFragment extends BaseFragment {

    private View rootView;
    private SeekBar seekBarChangeBrightness;
    private GifEditorBrightnessImageCallBack brightnessImageCallBack;
    private GPUImageBrightnessFilter brightness;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brightness = new GPUImageBrightnessFilter();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_editor_brightness, null, false);
        initIdView();
        initActionView();
        return rootView;
    }

    private void initActionView() {
        seekBarChangeBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                brightnessImageCallBack.intiBrightnessImage((float)(seekBar.getProgress() - 250)/250);
            }
        });
    }

    private void initIdView() {
        seekBarChangeBrightness = (SeekBar) rootView.findViewById(R.id.seekBarChangeBrightness);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorBrightnessFragment.class.toString());
    }

    public void initBrightnessImageCallBack(GifEditorBrightnessImageCallBack brightnessImageCallBack) {
        this.brightnessImageCallBack = brightnessImageCallBack;
    }
}
