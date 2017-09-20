package com.gifmaker.gifeditor.ui.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by longdg on 2017-08-08.
 */

public class GifSlidePreviewAdapter extends FragmentStatePagerAdapter {

    public GifSlidePreviewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
