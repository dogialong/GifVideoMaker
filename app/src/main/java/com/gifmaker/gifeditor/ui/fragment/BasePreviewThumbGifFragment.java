package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.model.ImageThumbGif;
import com.gifmaker.gifeditor.model.controller.GifHelper;
import com.gifmaker.gifeditor.ui.activity.CutVideoInputCreatGifActivity;
import com.gifmaker.gifeditor.ui.adatper.ThumbGifFrameAdapter;
import com.gifmaker.gifeditor.ui.custom.GridLayoutManagerCustom;
import com.gifmaker.gifeditor.ui.view.RangeSeekBar;
import com.gifmaker.gifeditor.ui.view.TimeDurationListener;

import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by mr.logic on 7/27/2017.
 */

public class BasePreviewThumbGifFragment extends  BaseFragment {

    protected GifHelper mGifHelper;
    protected ThumbGifFrameAdapter mThumbGifFrameAdapter;
    protected ArrayList<ImageThumbGif> mImageThumbGifs;
    private RecyclerView mRecyclerView;
    private VideoView mVideoView;
    public RangeSeekBar rangeSeekBar;
    private GridLayoutManagerCustom customLayoutManager;
    public int minSecondValue = 0;
    public int maxSecondValue = 0;
    public int timeCurrentVideo = 0;
    public TextView tvTimeStart,tvTimeEnd,tvTimeMiddle;
    private TimeDurationListener timeDurationListener;
    private PreviewVideoFragment previewVideoFragment;
    private MyHandler myHandler;
    private MyRunable myRunable;
    public BasePreviewThumbGifFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cut_preview_thumb_gif, null, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        previewVideoFragment = (PreviewVideoFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.frameReviewData);
        mVideoView = previewVideoFragment.videoView;
        rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.rangeSeekBar);
        maxSecondValue = previewVideoFragment.gifHelper.getDurationFileVideo()*1000;
        myHandler = new MyHandler();

        tvTimeEnd = (TextView) view.findViewById(R.id.tvTimeEnd);
        tvTimeStart = (TextView) view.findViewById(R.id.tvTimeStart);
        tvTimeStart.setText("00:00:000");
        tvTimeEnd.setText(formatHoursAndMinutes(maxSecondValue/1000));
        timeDurationListener = ((CutVideoInputCreatGifActivity) getActivity()).timeDurationListener;
        rangeSeekBar.setRangeValues(0,(double)maxSecondValue/1000);
        rangeSeekBar.setValuePerSeconde((double)maxSecondValue/1000);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                minSecondValue = (int) minValue*1000;
                maxSecondValue = (int) maxValue *1000;
                timeDurationListener.getMaxValue(maxSecondValue);
                timeDurationListener.getMinValue(minSecondValue);
                previewVideoFragment.playVideo(minSecondValue);

            }

            @Override
            public void onRangeSeekBarRealtimeValuesChange(RangeSeekBar bar, Number minValue, Number maxValue) {
                tvTimeEnd.setText(formatHoursAndMinutes((int)maxValue));
                tvTimeStart.setText(formatHoursAndMinutes((int)minValue));
            }
        });
        myRunable = new MyRunable(new TimeVideoListener() {
            @Override
            public void process() {
                timeCurrentVideo = Math.abs((previewVideoFragment.gifHelper.getDurationFileVideo()
                        - mVideoView.getCurrentPosition())/1000);
            }
        });
        myHandler.postDelayed(myRunable,1000);

        previewVideoFragment.buttonPauseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewVideoFragment.buttonPauseVideo.setVisibility(View.GONE);
                previewVideoFragment.videoView.seekTo(minSecondValue);
                previewVideoFragment.videoView.start();
            }
        });

        customLayoutManager = new GridLayoutManagerCustom(getContext()){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        customLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.reyImageOfData);
        mRecyclerView.setLayoutManager(customLayoutManager);

        return view;
    }

    public void initTimeDurationCallback(TimeDurationListener timeDurationListener) {
        this.timeDurationListener = timeDurationListener;
    }
    public void unregisterTimeDurationCallback () {
        this.timeDurationListener = null;
    }
    protected  void showDataView(){
        mRecyclerView.setAdapter(mThumbGifFrameAdapter);
    }

    protected  void reFreshDataView(){
        mThumbGifFrameAdapter.notifyDataSetChanged();
    }

    public static String formatHoursAndMinutes(int totalMinutes) {
        long minutes = (int) (totalMinutes / 60);
        int seconds = (int) ((totalMinutes) % 60);
        int miliseconds = (int) (totalMinutes % 1000);
        String result = String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds) + "." +  String.format("%03d",miliseconds);
        return result;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myHandler !=null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myHandler !=null) {
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }
        if (myRunable != null) {
            myRunable.unregisterListener();
        }
    }
    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage: " + msg);
        }
    }
    private static class MyRunable implements Runnable {
        TimeVideoListener timeVideoListener;
        public MyRunable (TimeVideoListener timeVideoListener) {
            this.timeVideoListener = timeVideoListener;
        }
        @Override
        public void run() {
           this.timeVideoListener.process();
        }
        public void unregisterListener() {
            this.timeVideoListener = null;
        }
    }
    private interface TimeVideoListener {
        public void process ();
    }
}

