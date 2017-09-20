package com.gifmaker.gifeditor.ui.adatper;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gifmaker.gifeditor.R;

import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;


/**
 * Created by longdg on 2017-08-02.
 */

public class ListMyGifAdapter extends RecyclerView.Adapter<ListMyGifAdapter.ViewHolder> {
    private ArrayList<String> listPathMyGif;
    private View view;
    private Context mContext;
    private GifDrawable gifFromFile;
    private OnItemClickListener mItemClickListener;
    private int size;
    private int sizeMargin = 5;
    public ListMyGifAdapter(Context mContext, ArrayList<String> listPathMyGif) {
        this.listPathMyGif = listPathMyGif;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_gif, parent, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size-sizeMargin, size-sizeMargin);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            gifFromFile = new GifDrawable(listPathMyGif.get(position));
            gifFromFile.stop();
            holder.imageMyGif.setImageDrawable(gifFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listPathMyGif.size();
    }

    public void setLayoutParams(int size) {
        this.size = size;
    }

    public void notifyDataSetChanged(ArrayList<String> listPathGif){
        listPathMyGif.clear();
        listPathMyGif.addAll(listPathGif);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        pl.droidsonroids.gif.GifImageView imageMyGif;
        ImageView infoGif;


        public ViewHolder(View itemView) {
            super(itemView);
            imageMyGif = (pl.droidsonroids.gif.GifImageView) itemView.findViewById(R.id.imageMyGif);
            infoGif = (ImageView) itemView.findViewById(R.id.infoGif);
            imageMyGif.setOnClickListener(this);
            infoGif.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                switch (v.getId()) {
                    case R.id.imageMyGif:
                        mItemClickListener.onClickListener(imageMyGif, getAdapterPosition());
                        break;
                    case R.id.infoGif:
                        mItemClickListener.onClickListener(infoGif, getAdapterPosition());
                        break;
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onClickListener(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
