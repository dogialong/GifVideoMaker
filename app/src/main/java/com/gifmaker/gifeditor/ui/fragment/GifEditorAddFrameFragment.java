package com.gifmaker.gifeditor.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;
import com.gifmaker.gifeditor.ui.adatper.FrameAddToGifItemAdapter;
import com.gifmaker.gifeditor.ui.adatper.ViewPagerAddFrameAdapter;
import com.gifmaker.gifeditor.ui.view.GifEditorAddFrameCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorAddFrameItemCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorAddFrameFragment extends BaseFragment implements GifEditorAddFrameItemCallBack
        ,ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener {
    private View rootView;
    private ViewPager viewPagerFrame;
    private GifEditorAddFrameItemCallBack gifEditorAddFrameItemCallBack;
    private TabLayout tabLayoutFrame;
    private ViewPagerAddFrameAdapter viewPagerAddFrameAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_editor_add_frame, null, false);
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
        rootView = inflater.inflate(R.layout.fragment_editor_add_frame, null, false);

        initIdView();
        return rootView;
    }


    private void initIdView() {

        tabLayoutFrame = (TabLayout) rootView.findViewById(R.id.tab_layoutFrame);
        viewPagerFrame = (ViewPager) rootView.findViewById(R.id.pager_frame);
        viewPagerAddFrameAdapter = new ViewPagerAddFrameAdapter(getContext(), getActivity().getSupportFragmentManager(), this);
        viewPagerFrame.setAdapter(viewPagerAddFrameAdapter);
        tabLayoutFrame.setupWithViewPager(viewPagerFrame);
        for (int i = 0; i < tabLayoutFrame.getTabCount(); i++) {
            TabLayout.Tab taframes = tabLayoutFrame.getTabAt(i);
            taframes.setCustomView(viewPagerAddFrameAdapter.getTabView(i));
        }
        viewPagerFrame.setCurrentItem(0);
        tabLayoutFrame.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPagerFrame.addOnPageChangeListener(this);
        tabLayoutFrame.setOnTabSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorAddFrameFragment.class.toString());
    }

    public void initAddFrameImageCallBack(GifEditorAddFrameItemCallBack gifEditorAddFrameItemCallBack) {
        this.gifEditorAddFrameItemCallBack = gifEditorAddFrameItemCallBack;
    }

    @Override
    public void indexFrameAddToGif(int indexOfListFrame, int indexFrame) {
        gifEditorAddFrameItemCallBack.indexFrameAddToGif(indexOfListFrame, indexFrame);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabLayoutFrame.setScrollX(tabLayoutFrame.getWidth());
        tabLayoutFrame.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPagerFrame.setCurrentItem(tab.getPosition());

        LinearLayout layoutCustomTitle = (LinearLayout) tab.getCustomView();
        TextView textnameFrame = (TextView)layoutCustomTitle.findViewById(R.id.frame_name);

        textnameFrame.setBackgroundResource(R.drawable.bolder_textview);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        LinearLayout layoutCustomTitle = (LinearLayout) tab.getCustomView();
        TextView textnameFrame = (TextView)layoutCustomTitle.findViewById(R.id.frame_name);

        textnameFrame.setBackgroundResource(0);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
