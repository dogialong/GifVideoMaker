package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;
import com.gifmaker.gifeditor.ui.adatper.StickerAdapter;
import com.gifmaker.gifeditor.ui.adatper.ViewPagerAddStickerAdapter;
import com.gifmaker.gifeditor.ui.view.GifEditorAddFrameItemCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorAddStickerItemCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorAddStickerFragment extends BaseFragment implements GifEditorAddStickerItemCallBack,ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener  {

    private View rootView;
    private ViewPager viewPagerSticker;
    private ViewPagerAddStickerAdapter viewPagerAddStickerAdapter;
    private TabLayout tabLayoutSticker;
    private GifEditorAddStickerItemCallBack gifEditorAddStickerItemCallBack;
    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorAddStickerFragment.class.toString());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_sticker, null, false);
            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
            if (fragments != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                for (Fragment f : fragments) {
                    if (f instanceof GifEditorListAddFrameFragment) {
                        ft.remove(f);
                    }
                }
                ft.commitAllowingStateLoss();
            }


        }
        rootView = inflater.inflate(R.layout.fragment_sticker, null, false);
        initView();
        initData();
        return rootView;
    }

    private void initData() {
        viewPagerAddStickerAdapter = new ViewPagerAddStickerAdapter(this.getContext()
                , getActivity().getSupportFragmentManager(),this);
        viewPagerSticker.setAdapter(viewPagerAddStickerAdapter);
        tabLayoutSticker.setupWithViewPager(viewPagerSticker);
        for (int i = 0; i < tabLayoutSticker.getTabCount(); i++) {
            TabLayout.Tab taframes = tabLayoutSticker.getTabAt(i);
            taframes.setCustomView(viewPagerAddStickerAdapter.getTabView(i));
        }
        tabLayoutSticker.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPagerSticker.addOnPageChangeListener(this);
        tabLayoutSticker.setOnTabSelectedListener(this);

    }
    private void initView () {
        viewPagerSticker = (ViewPager)rootView.findViewById(R.id.viewpager_sticker);
        tabLayoutSticker = (TabLayout)rootView.findViewById(R.id.tab_layoutSticker);


//        stickerListView.setLayoutManager(new GridLayoutManager(getContext(), 2,GridLayoutManager.HORIZONTAL,false));

    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    public void initAddStickerCallBack(GifEditorAddStickerItemCallBack gifEditorAddStickerItemCallBack) {
        this.gifEditorAddStickerItemCallBack = gifEditorAddStickerItemCallBack;
    }
    @Override
    public void indexStickerAddToGif(int indexOfListSticker, int indexsticker) {
        Log.e("Linh", indexOfListSticker + " ssa"+ indexsticker);
        gifEditorAddStickerItemCallBack.indexStickerAddToGif(indexOfListSticker,indexsticker);
    }
}
