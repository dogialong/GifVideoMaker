package com.gifmaker.gifeditor.ui.adatper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.fragment.GifSlideShowFragment;

/**
 * Created by PhungVanQuang on 7/27/2017.
 */

public class SlideShowAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public SlideShowAdapter(Context context,FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return GifSlideShowFragment.newInsFragmentWorkout(position);
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}