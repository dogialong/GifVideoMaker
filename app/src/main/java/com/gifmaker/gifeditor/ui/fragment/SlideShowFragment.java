package com.gifmaker.gifeditor.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.adatper.SlideShowAdapter;

import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by mr.logic on 7/27/2017.
 */

public class SlideShowFragment extends BaseFragment {

    private View slideShowView;
    private TextView[] dots;
    private LinearLayout layoutDots;
    private AutoScrollViewPager viewPager;
    private SlideShowAdapter slideShowAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (slideShowView == null) {
            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
            if (fragments != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                for (Fragment f : fragments) {
                    if (f instanceof GifSlideShowFragment) {
                        ft.remove(f);
                    }
                }
                ft.commitAllowingStateLoss();
            }
            slideShowView = inflater.inflate(R.layout.fragment_slide_show, null, false);
            slideShowView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        initIdView();
        initActionView();
        return slideShowView;
    }


    private void initIdView() {
        layoutDots = (LinearLayout) slideShowView.findViewById(R.id.layoutDots);
        viewPager = (AutoScrollViewPager) slideShowView.findViewById(R.id.viewPager);
        slideShowAdapter = new SlideShowAdapter(getContext(), getActivity().getSupportFragmentManager());
        viewPager.setAdapter(slideShowAdapter);
        viewPager.addOnPageChangeListener(new CircularViewPagerHandler(viewPager));
        viewPager.startAutoScroll(4000);
        viewPager.setScrollDurationFactor(10);
        viewPager.setInterval(3000);
        viewPager.setOffscreenPageLimit(1);
        addBottomDots(0);
        viewPager.setCurrentItem(0);
        slideShowAdapter.notifyDataSetChanged();
    }

    private void initActionView() {
    }

    private void addBottomDots(int currentImage) {
        int totalPageSlideShow = 0;
        try {
            totalPageSlideShow = getContext().getResources().getStringArray(R.array.list_gif_slide_show).length;
        } catch (Exception e) {

        }
        dots = new TextView[totalPageSlideShow];
        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.WHITE);
            layoutDots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentImage].setTextColor(Color.GRAY);
    }

    private class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
        private ViewPager mViewPager;
        private int mCurrentPosition;
        private int mScrollState;

        public CircularViewPagerHandler(final ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onPageSelected(final int position) {
            mCurrentPosition = position;
            addBottomDots(position);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            handleScrollState(state);
            mScrollState = state;
        }

        private void handleScrollState(final int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                setNextItemIfNeeded();
            }
        }

        private void setNextItemIfNeeded() {
            if (!isScrollStateSettling()) {
                handleSetNextItem();
            }
        }

        private boolean isScrollStateSettling() {
            return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
        }

        private void handleSetNextItem() {
            final int lastPosition = mViewPager.getAdapter().getCount() - 1;
            if (mCurrentPosition == 0) {
                mViewPager.setCurrentItem(lastPosition, true);
            } else if (mCurrentPosition == lastPosition) {
                mViewPager.setCurrentItem(0, false);
            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewPager.stopAutoScroll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewPager = null;
    }
}
