package com.gifmaker.gifeditor.ui.fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.model.controller.GifHelper;
import com.gifmaker.gifeditor.ui.view.VideoViewUser;
import com.rey.material.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewVideoFragment extends BasePreviewThumbGifFragment implements VideoViewUser {

    private View previewVideoView;

    public VideoView videoView;
    public ImageView buttonPauseVideo;
    private String dataPath;
    private MyRunable myRunable;
    private MyHandler myHandler;
    public GifHelper gifHelper;
    public PreviewVideoFragment() {
    }

    public static PreviewVideoFragment getInstance(String uri) {
        PreviewVideoFragment mFragment = new PreviewVideoFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("dataPath", uri);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataPath = getArguments().getString("dataPath");
        gifHelper = new GifHelper(getActivity(),dataPath);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        previewVideoView = inflater.inflate(R.layout.fragment_preview_video, container, false);
        initIdView();
        initActionView();
        return previewVideoView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initIdView() {
        videoView = (VideoView) previewVideoView.findViewById(R.id.videoView);
        buttonPauseVideo = (ImageView) previewVideoView.findViewById(R.id.buttonPauseVideo);
        myHandler = new MyHandler();
        myRunable = new MyRunable(new OnVideoRunListener() {
            @Override
            public void process() {
                if(!videoView.isPlaying()) {
                    buttonPauseVideo.setVisibility(View.VISIBLE);
                } else {
                    buttonPauseVideo.setVisibility(View.GONE);
                }
                myHandler.postDelayed(myRunable,1000);
            }
        });
    }

    private void initActionView() {
        Uri uriPathData = Uri.parse(dataPath);
        videoView.setVideoURI(uriPathData);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
               myHandler.postDelayed(myRunable,1000);
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (videoView.isPlaying()) {
                    buttonPauseVideo.setVisibility(View.VISIBLE);
                    videoView.pause();
                } else {
                    buttonPauseVideo.setVisibility(View.GONE);
                    videoView.start();
                }
                return false;
            }
        });

    }

    @Override
    public void playVideo(int seekto) {
        videoView.pause();
        videoView.seekTo(seekto);
    }
    private static class MyHandler extends Handler {
        @Override
        public String getMessageName(Message message) {
            return super.getMessageName(message);
        }
    }
    private static class MyRunable implements Runnable {
        OnVideoRunListener onVideoRunlistener;
        public MyRunable (OnVideoRunListener onVideoRunListener) {
            this.onVideoRunlistener = onVideoRunListener;
        }
        @Override
        public void run() {
            onVideoRunlistener.process();
        }
        private void unregisterListener() {
            this.onVideoRunlistener = null;
        }
    }
    private interface OnVideoRunListener {
        void process();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (myRunable != null) {
            myRunable.unregisterListener();
        }
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }
    }
}
