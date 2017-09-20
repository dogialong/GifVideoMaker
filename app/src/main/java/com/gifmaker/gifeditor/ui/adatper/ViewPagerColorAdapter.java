package com.gifmaker.gifeditor.ui.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gifmaker.gifeditor.ui.fragment.GifEditorColorPickerFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEdittorFragmentCustomColor;
import com.gifmaker.gifeditor.ui.view.GifEditorColorSelectCallBack;

/**
 * Created by linh on 8/23/2017.
 */

public class ViewPagerColorAdapter extends FragmentStatePagerAdapter {

    private int heightKeyboard;
    private int colorCode;
    private GifEditorColorSelectCallBack gifEditorColorSelectCallBack;

    public ViewPagerColorAdapter(FragmentManager fm, int heightKeyboard, int colorCode, GifEditorColorSelectCallBack gifEditorColorSelectCallBack) {

        super(fm);
        this.heightKeyboard = heightKeyboard;
        this.colorCode = colorCode;
        this.gifEditorColorSelectCallBack = gifEditorColorSelectCallBack;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                GifEditorColorPickerFragment gifEditorColorPickerFragment = GifEditorColorPickerFragment.newInstance(heightKeyboard);
                gifEditorColorPickerFragment.initColorSelect(gifEditorColorSelectCallBack);
                frag = gifEditorColorPickerFragment;
                break;
            case 1:
                GifEdittorFragmentCustomColor gifEdittorFragmentCustomColor = GifEdittorFragmentCustomColor.newInstance(colorCode);
                gifEdittorFragmentCustomColor.initColorSelect(gifEditorColorSelectCallBack);
                frag = gifEdittorFragmentCustomColor;
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
