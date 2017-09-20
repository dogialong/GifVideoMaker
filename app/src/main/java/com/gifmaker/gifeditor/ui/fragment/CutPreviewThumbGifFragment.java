package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.gifmaker.gifeditor.model.controller.GifHelper;
import com.gifmaker.gifeditor.presenter.GifFramePresenter;
import com.gifmaker.gifeditor.presenter.ImageLoaderPresenter;
import com.gifmaker.gifeditor.ui.activity.CutVideoInputCreatGifActivity;
import com.gifmaker.gifeditor.ui.adatper.ThumbGifFrameAdapter;
import com.gifmaker.gifeditor.ui.view.PreviewThumbGifViewUser;

import java.io.File;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class CutPreviewThumbGifFragment extends BasePreviewThumbGifFragment implements PreviewThumbGifViewUser{
    private final static String TAG = CutVideoInputCreatGifActivity.class.getSimpleName();

    public String mPathInputMedia;
    private GifFramePresenter mGifFramePresenter;
    private boolean checkFragmentLife = true;

    public static CutPreviewThumbGifFragment getInstance(String pathInpuMedia){
        CutPreviewThumbGifFragment cutPreviewThumbGifFragment = new CutPreviewThumbGifFragment();
        Bundle args = new Bundle();
        args.putString("pathInpuMedia",pathInpuMedia);
        cutPreviewThumbGifFragment.setArguments(args);
        return cutPreviewThumbGifFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPathInputMedia = getArguments().getString("pathInpuMedia");
        mGifHelper = new GifHelper(getActivity(),mPathInputMedia);
        mGifFramePresenter = new GifFramePresenter(mGifHelper,this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mThumbGifFrameAdapter = new ThumbGifFrameAdapter() {
            @Override
            public int getCountThumb() {
                return   mGifFramePresenter.getTotalThumbGif();
            }

            @Override
            public void onBinImageView(final ImageView imageView, int position) {
                if (imageView!= null) {
                    mGifFramePresenter.loadAutoThumbGif(imageView,position);
                }
            }
        };

        showDataView();
    }


    @Override
    public void displayGifThumb(ImageView imageView, File fileThumbGif) {
        if (imageView != null || fileThumbGif!= null) {
            ImageLoaderPresenter.displayThumbImage(getActivity(),imageView,fileThumbGif.getPath(),mGifHelper.widthImageView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkFragmentLife = false;
    }
}
