package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.adatper.FrameAddToGifItemAdapter;
import com.gifmaker.gifeditor.ui.adatper.StickerAdapter;
import com.gifmaker.gifeditor.ui.view.GifEditorAddStickerItemCallBack;

/**
 * Created by linh on 8/17/2017.
 */

public class GifEditorListAddStickFragment extends Fragment {
    private View rootView;
    private RecyclerView recyclerViewItemSticker;
    private int pos;
    private GifEditorAddStickerItemCallBack gifEditorAddStickerItemCallBack;

    public static GifEditorListAddStickFragment newInsFragmentWorkout(GifEditorListAddStickFragment mFragment, int pos) {
        Bundle mBundle = new Bundle();
        mBundle.putInt("pos", pos);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    public GifEditorListAddStickFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("pos");
        Log.e("pos", pos + " ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.edit_list_add_sticker_fragment, null, false);

        recyclerViewItemSticker = (RecyclerView) rootView.findViewById(R.id.recyclerview_item_sticker);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
        recyclerViewItemSticker.setLayoutManager(gridLayoutManager);
        recyclerViewItemSticker.setHasFixedSize(true);
        initItem(pos);
        return rootView;
    }


    private void initItem(int position) {
        int resourceId = getResources().getIdentifier("stick" + position, "array", getContext().getPackageName());
        String[] listFrame = getResources().getStringArray(resourceId);
        StickerAdapter stickerAdapter = new StickerAdapter(getContext(), listFrame);
        recyclerViewItemSticker.setAdapter(stickerAdapter);
        stickerAdapter.setOnItemClickListener(new StickerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                gifEditorAddStickerItemCallBack.indexStickerAddToGif(pos, position);
                Log.e("linh", pos + " ss" + position);
            }
        });

    }

    public void initActionAddStick(GifEditorAddStickerItemCallBack gifEditorAddStickerItemCallBack) {
        this.gifEditorAddStickerItemCallBack = gifEditorAddStickerItemCallBack;
    }


}
