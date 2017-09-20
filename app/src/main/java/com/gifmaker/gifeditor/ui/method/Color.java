package com.gifmaker.gifeditor.ui.method;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.gifmaker.gifeditor.R;

import java.util.ArrayList;

/**
 * Created by PhungVanQuang on 8/5/2017.
 */

public class Color {

    public static ArrayList<Integer> intitListColorText(Context context) {
        ArrayList<Integer> colorPickerColors = new ArrayList<>();
        ArrayList<Integer> colorPickerColorsMax = new ArrayList<>();
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_1));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_2));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_3));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_4));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_5));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_6));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_7));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_8));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_9));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_10));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_11));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_12));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_13));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_14));
        colorPickerColors.add(context.getResources().getColor(R.color.text_color_15));

        return colorPickerColors;
    }
}
