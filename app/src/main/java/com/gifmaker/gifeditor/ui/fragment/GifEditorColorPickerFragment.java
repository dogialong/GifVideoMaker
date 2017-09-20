package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.adatper.ColorPickerAdapter;
import com.gifmaker.gifeditor.ui.method.Color;
import com.gifmaker.gifeditor.ui.view.GifEditorAddStickerItemCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorColorSelectCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linh on 8/23/2017.
 */

public class GifEditorColorPickerFragment extends Fragment {
    View rootView;
    private RecyclerView viewColor;
    private ColorPickerAdapter gridAdapter;
    private int heightKeyboard;
    private GifEditorColorSelectCallBack gifEditorColorSelectCallBack;

    public GifEditorColorPickerFragment() {
    }

    public static GifEditorColorPickerFragment newInstance(int heightKeyboard) {
        GifEditorColorPickerFragment gifEditorColorPickerFragment = new GifEditorColorPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("height", heightKeyboard);
        gifEditorColorPickerFragment.setArguments(bundle);
        return gifEditorColorPickerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Display mDisplay = getActivity().getWindowManager().getDefaultDisplay();

        final int width = mDisplay.getWidth();
        heightKeyboard = getArguments().getInt("height");
        int widthMax = width /5;
        int heightMax = heightKeyboard/3;

        int margin = (width - 5 * widthMax) / 5;
        rootView = inflater.inflate(R.layout.fragment_color_picker, null, false);
        viewColor = (RecyclerView) rootView.findViewById(R.id.grid_view_color);
        ArrayList<Integer> listColor = Color.intitListColorText(getContext());
        gridAdapter = new ColorPickerAdapter(getContext(), listColor,widthMax, heightMax, margin);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
        viewColor.setLayoutManager(gridLayoutManager);
        viewColor.setHasFixedSize(true);
        viewColor.setAdapter(gridAdapter);
        gridAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                gifEditorColorSelectCallBack.color(colorCode);
            }
        });
        return rootView;

    }

    public void initColorSelect(GifEditorColorSelectCallBack gifEditorColorSelectCallBack) {
        this.gifEditorColorSelectCallBack = gifEditorColorSelectCallBack;
    }

}
