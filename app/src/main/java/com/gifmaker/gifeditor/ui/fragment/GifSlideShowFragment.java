package com.gifmaker.gifeditor.ui.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gifmaker.gifeditor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GifSlideShowFragment extends Fragment {

    private View rootView;
    private int pos;
    private ImageView imageViewSlide;

    public static GifSlideShowFragment newInsFragmentWorkout(int pos){
        GifSlideShowFragment mFragment = new GifSlideShowFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt("pos", pos);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    public GifSlideShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos  = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_gif_slide_show, null, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        initIdView();
        iniSlideView(pos);
        return rootView;
    }

    private void initIdView() {
        imageViewSlide = (ImageView) rootView.findViewById(R.id.imageViewSlide);
    }

    private void iniSlideView(int pos) {

        switch (pos){
            case 0 :
                imageViewSlide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.slide1));
                break;
            case 1 :
                imageViewSlide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.slide2));
                break;
            case 2 :
                imageViewSlide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.slide3));
                break;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
