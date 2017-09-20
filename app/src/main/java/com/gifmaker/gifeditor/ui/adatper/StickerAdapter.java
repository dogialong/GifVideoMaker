package com.gifmaker.gifeditor.ui.adatper;


import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gifmaker.gifeditor.R;

/**
 * Created by longdg on 2017-08-02.
 */

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    private View view;
    private String[] listSticker;
    private Context context;
    private OnItemClickListener mItemClickListener;


    public StickerAdapter(Context context, String[] listSticker) {
        this.context = context;
        this.listSticker = listSticker;

    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_sticker_add_to_gif, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(listSticker[position], "drawable", context.getPackageName());
        holder.item.setImageResource(resourceId);

    }

    @Override
    public int getItemCount() {
        if (listSticker != null) {
            return listSticker.length;
        } else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView item;


        public ViewHolder(View itemView) {
            super(itemView);
            item = (ImageView) itemView.findViewById(R.id.stickerAddtoGif);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
         if (mItemClickListener!=null){
            mItemClickListener.onItemClick(v,getAdapterPosition());

        }}
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
