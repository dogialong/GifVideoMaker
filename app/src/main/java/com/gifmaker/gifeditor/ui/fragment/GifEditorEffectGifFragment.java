package com.gifmaker.gifeditor.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;
import com.gifmaker.gifeditor.ui.adatper.EffectImageGifAdapter;
import com.gifmaker.gifeditor.ui.view.GifEditorEffectImageCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.gifmaker.gifeditor.ui.view.GPUImageFilterTools.createFilterForType;
import static com.gifmaker.gifeditor.ui.view.GPUImageFilterTools.getListFilter;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorEffectGifFragment extends BaseFragment {

    private View rootView;
    private RecyclerView listEffectGif;
    private EffectImageGifAdapter effectImageGifAdapter;
    private GifEditorEffectImageCallBack effectImageCallBack;
    private String[] listnameImage;

    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorEffectGifFragment.class.toString());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_editor_effects_gif, null, false);
        initIdView();
        return rootView;
    }

    private void initIdView() {
        listEffectGif = (RecyclerView) rootView.findViewById(R.id.listEffectGif);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        listEffectGif.setLayoutManager(layoutManager);
        listEffectGif.setHasFixedSize(true);
         listnameImage = getResources().getStringArray(R.array.name_effects);
        Log.e("size",listnameImage.length+ listnameImage[1]);
        effectImageGifAdapter = new EffectImageGifAdapter(getContext(), getListFilter(),listnameImage);
        listEffectGif.setAdapter(effectImageGifAdapter);
        effectImageGifAdapter.setOnItemClickListener(new EffectImageGifAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    effectImageCallBack.setFiltersImage(null);
                }
                effectImageCallBack.setFiltersImage(createFilterForType(getListFilter().filters.get(position)));
            }
        });
    }

    public void initEffectImageCallBack(GifEditorEffectImageCallBack effectImageCallBack) {
        this.effectImageCallBack = effectImageCallBack;
    }

}
