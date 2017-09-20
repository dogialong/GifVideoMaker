package com.gifmaker.gifeditor.ui.view;

/**
 * Created by longdg on 2017-08-01.
 */

public interface OnRangeSeekBarListener {
    void onCreate(RangeSeekBar rangeSeekBarView, int index, float value);

    void onSeek(RangeSeekBar rangeSeekBarView, int index, float value);

    void onSeekStart(RangeSeekBar rangeSeekBarView, int index, float value);

    void onSeekStop(RangeSeekBar rangeSeekBarView, int index, float value);
}
