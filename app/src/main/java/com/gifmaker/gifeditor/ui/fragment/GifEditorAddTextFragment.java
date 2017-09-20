package com.gifmaker.gifeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;
import com.gifmaker.gifeditor.ui.adatper.ColorPickerAdapter;

import java.util.ArrayList;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorAddTextFragment extends BaseFragment {
    ColorPickerAdapter colorAdapter;
    private ArrayList<Integer> colorPickerColors;
    RecyclerView recyclerViewColor,recyclerViewFont;


    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorAddTextFragment.class.toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorPickerColors = new ArrayList<>();
        colorPickerColors.add(getResources().getColor(R.color.text_color_1));
        colorPickerColors.add(getResources().getColor(R.color.text_color_2));
        colorPickerColors.add(getResources().getColor(R.color.text_color_3));
        colorPickerColors.add(getResources().getColor(R.color.text_color_4));
        colorPickerColors.add(getResources().getColor(R.color.text_color_5));
        colorPickerColors.add(getResources().getColor(R.color.text_color_6));
        colorPickerColors.add(getResources().getColor(R.color.text_color_7));
        colorPickerColors.add(getResources().getColor(R.color.text_color_8));
        colorPickerColors.add(getResources().getColor(R.color.text_color_9));
        colorPickerColors.add(getResources().getColor(R.color.text_color_10));
        colorPickerColors.add(getResources().getColor(R.color.text_color_11));
        colorPickerColors.add(getResources().getColor(R.color.text_color_12));
        colorPickerColors.add(getResources().getColor(R.color.text_color_13));
        colorPickerColors.add(getResources().getColor(R.color.text_color_14));
        colorPickerColors.add(getResources().getColor(R.color.text_color_15));


        // list font


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_text, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }
    private void initView (View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerFont = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewColor = (RecyclerView) view.findViewById(R.id.listColor);
        recyclerViewColor.setLayoutManager(layoutManager);
        recyclerViewFont = (RecyclerView) view.findViewById(R.id.recycleFont);
        recyclerViewFont.setLayoutManager(layoutManagerFont);
        //colorAdapter = new ColorPickerAdapter(getContext(), colorPickerColors);

    }
}
