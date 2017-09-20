package com.gifmaker.gifeditor.ui.adatper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.fragment.GifEditorListAddFrameFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorListAddStickFragment;
import com.gifmaker.gifeditor.ui.view.GifEditorAddStickerItemCallBack;

/**
 * Created by linh on 8/17/2017.
 */

public class ViewPagerAddStickerAdapter extends FragmentStatePagerAdapter {
    private int[] idImageSticker;
    private Context context;
    private GifEditorAddStickerItemCallBack gifEditorAddStickerItemCallBack;
    public ViewPagerAddStickerAdapter(Context context,FragmentManager fm,GifEditorAddStickerItemCallBack gifEditorAddStickerItemCallBack) {
        super(fm);
        this.context = context;
        this.idImageSticker = new int[]{
                R.drawable.bulldel,
                R.drawable.cony,
                R.drawable.emoij,
                R.drawable.fruit,
                R.drawable.hallowen,
                R.drawable.hat,
                R.drawable.heart,
                R.drawable.moster,

        };
        this.gifEditorAddStickerItemCallBack = gifEditorAddStickerItemCallBack;
    }

    @Override
    public Fragment getItem(int position) {
        GifEditorListAddStickFragment gifEditorListAddStickFragment = new GifEditorListAddStickFragment();
        gifEditorListAddStickFragment.newInsFragmentWorkout(gifEditorListAddStickFragment,position);
        gifEditorListAddStickFragment.initActionAddStick(gifEditorAddStickerItemCallBack);
        return gifEditorListAddStickFragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return idImageSticker.length;
    }
    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_list_image_sticker_in_group_sticker, null);
        ImageView imgSticker = (ImageView) v.findViewById(R.id.img_sticker);
        imgSticker.setBackgroundResource(idImageSticker[position]);
        return v;
    }
}
