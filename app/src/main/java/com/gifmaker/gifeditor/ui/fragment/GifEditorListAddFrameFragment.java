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
import com.gifmaker.gifeditor.ui.view.GifEditorAddFrameItemCallBack;

/**
 * Created by linh on 8/16/2017.
 */

public class GifEditorListAddFrameFragment extends Fragment {
    private View rootView;
    private RecyclerView recyclerViewItemFrame;
    private int pos;
    private GifEditorAddFrameItemCallBack gifEditorAddFrameItemCallBack;

    public static GifEditorListAddFrameFragment newInsFragmentWorkout(GifEditorListAddFrameFragment mFragment, int pos) {
        Bundle mBundle = new Bundle();
        mBundle.putInt("pos", pos);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    public GifEditorListAddFrameFragment() {

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
            rootView = inflater.inflate(R.layout.edit_list_add_frame_fragment, null, false);

        recyclerViewItemFrame = (RecyclerView) rootView.findViewById(R.id.recyclerview_item_frame);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
        recyclerViewItemFrame.setLayoutManager(gridLayoutManager);
        recyclerViewItemFrame.setHasFixedSize(true);
        initItem(pos);
        return rootView;
    }

    private void initItem(int position) {
        int resourceId = getResources().getIdentifier("list"+(position+1), "array", getContext().getPackageName());
        String []listFrame = getResources().getStringArray(resourceId);
        FrameAddToGifItemAdapter frameAddToGifItemAdapter = new FrameAddToGifItemAdapter(getContext(), listFrame);
        recyclerViewItemFrame.setAdapter(frameAddToGifItemAdapter);
        frameAddToGifItemAdapter.setOnItemClickListener(new FrameAddToGifItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                gifEditorAddFrameItemCallBack.indexFrameAddToGif(pos+1, position);
            }
        });
    }

    public void initActionAddFrame(GifEditorAddFrameItemCallBack gifEditorAddFrameItemCallBack) {
        this.gifEditorAddFrameItemCallBack = gifEditorAddFrameItemCallBack;
    }

}
