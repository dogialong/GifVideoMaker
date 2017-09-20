package com.gifmaker.gifeditor.ui.adatper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.utils.DeviceUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Ahmed Adel on 5/8/17.
 */

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {
    private View view;
    private Context context;
    private LayoutInflater inflater;
    private List<Integer> colorPickerColors;
    private OnColorPickerClickListener onColorPickerClickListener;
    private int heightMax;
    private int margin;
    private int widthMax;
    private ImageView imgItem;


    public ColorPickerAdapter(@NonNull Context context, @NonNull List<Integer> colorPickerColors, int widthMax, int heightkey, int margin) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorPickerColors = colorPickerColors;
        this.heightMax = heightkey;
        this.margin = margin;
        this.widthMax = widthMax;

    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.color_picker_item_list, parent, false);
        imgItem = (ImageView) view.findViewById(R.id.color_picker_view);

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imgItem.getLayoutParams();
        layoutParams.width = widthMax;
        layoutParams.height = heightMax;
        if (widthMax-margin >= heightMax) {
            lp.width = heightMax;
            lp.height = heightMax;
        } else {
            lp.width = widthMax-margin;
            lp.height = widthMax-margin;
        }
        view.setLayoutParams(layoutParams);
        imgItem.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemColor.setBackgroundColor(colorPickerColors.get(position));
    }

    @Override
    public int getItemCount() {
        return colorPickerColors.size();
    }


    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int colorCode);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView itemColor;

        public ViewHolder(View itemView) {
            super(itemView);
            itemColor = (ImageView) itemView.findViewById(R.id.color_picker_view);
            itemColor.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onColorPickerClickListener != null)
                onColorPickerClickListener.onColorPickerClickListener(colorPickerColors.get(getAdapterPosition()));
        }
    }
}
