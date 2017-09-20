package com.gifmaker.gifeditor.ui.adatper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.fragment.GifEditorAddFrameFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorListAddFrameFragment;
import com.gifmaker.gifeditor.ui.view.GifEditorAddFrameItemCallBack;

import java.util.ArrayList;

/**
 * Created by linh on 8/15/2017.
 */

public class ViewPagerAddFrameAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[];
    private Context context;
    private GifEditorAddFrameItemCallBack gifEditorAddFrameItemCallBack;

    public ViewPagerAddFrameAdapter(Context context,FragmentManager fm,GifEditorAddFrameItemCallBack gifEditorAddFrameItemCallBack) {
        super(fm);
        this.context = context;
        this.gifEditorAddFrameItemCallBack= gifEditorAddFrameItemCallBack;
        tabTitles = context.getResources().getStringArray(R.array.list_name_frames);
    }

    @Override
    public Fragment getItem(int position) {
        GifEditorListAddFrameFragment gifEditorListAddFrameFragment = new GifEditorListAddFrameFragment();
        gifEditorListAddFrameFragment.newInsFragmentWorkout(gifEditorListAddFrameFragment,position);
        gifEditorListAddFrameFragment.initActionAddFrame(gifEditorAddFrameItemCallBack);
        return gifEditorListAddFrameFragment;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public int getCount() {
        return context.getResources().getStringArray(R.array.list_name_frames).length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_list_name_frame_in_groupframe, null);
        TextView tv = (TextView) v.findViewById(R.id.frame_name);
        if(position == 0){
            tv.setBackgroundResource(R.drawable.bolder_textview);
        }
        tv.setText(tabTitles[position]);
        return v;
    }
}
