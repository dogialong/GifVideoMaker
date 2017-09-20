package com.gifmaker.gifeditor.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;
import com.gifmaker.gifeditor.ui.custom.ColorPickerView;
import com.gifmaker.gifeditor.ui.view.GifEditorColorSelectCallBack;

import java.util.Locale;

/**
 * Created by linh on 8/23/2017.
 */

public class GifEdittorFragmentCustomColor extends Fragment implements ColorPickerView.OnColorChangedListener
,View.OnClickListener{
    private View view;
    private ColorPickerView colorPickerView;
    private ImageView imgColor,imgBack;
    private TextView tvHexColor;
    private int colorCode;

    private GifEditorColorSelectCallBack gifEditorColorSelectCallBack;


    public static GifEdittorFragmentCustomColor newInstance(int colorCode) {
        GifEdittorFragmentCustomColor gifEdittorFragmentCustomColor = new GifEdittorFragmentCustomColor();
        Bundle bundle = new Bundle();
        bundle.putInt("colorCode", colorCode);
        gifEdittorFragmentCustomColor.setArguments(bundle);
        return gifEdittorFragmentCustomColor;
    }

    public GifEdittorFragmentCustomColor() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorCode = getArguments().getInt("colorCode");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_custom_color, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        colorPickerView = (ColorPickerView) view.findViewById(R.id.colorPickerView);
        imgColor = (ImageView) view.findViewById(R.id.imgColor);
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        tvHexColor = (TextView) view.findViewById(R.id.tvHexColor);
        imgColor.setBackgroundColor(colorPickerView.getColor());
        tvHexColor.setText("#" + Integer.toHexString(colorCode).toUpperCase(Locale.ENGLISH));
        colorPickerView.setOnColorChangedListener(this);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onColorChanged(int newColor) {

        imgColor.setBackgroundColor(colorPickerView.getColor());
        tvHexColor.setText("#" + Integer.toHexString(newColor).toUpperCase(Locale.ENGLISH));
        gifEditorColorSelectCallBack.color(newColor);
    }
    public void initColorSelect(GifEditorColorSelectCallBack gifEditorColorSelectCallBack) {
        this.gifEditorColorSelectCallBack = gifEditorColorSelectCallBack;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                ((GifEditorActivity) getActivity()).addTextColorPickerRecyclerView.setCurrentItem(0);
                break;
            default:break;
        }
    }
}
