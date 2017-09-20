package com.gifmaker.gifeditor.ui.adatper;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gifmaker.gifeditor.R;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class FrameAddToGifItemAdapter extends RecyclerView.Adapter<FrameAddToGifItemAdapter.ViewHolder> {
    private View view;
    private OnItemClickListener mItemClickListener;
    private Context context;
    private String[] listFrame;

    public FrameAddToGifItemAdapter(Context context, String[] listFrame) {
        this.context = context;
        this.listFrame = listFrame;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_frame_add_to_gif, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(listFrame[position], "drawable", context.getPackageName());
        holder.imageFrameAddToGif.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        if (listFrame != null) {
            return listFrame.length;
        } else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageFrameAddToGif;

        public ViewHolder(View itemView) {
            super(itemView);
            imageFrameAddToGif = (ImageView) itemView.findViewById(R.id.imageFrameAddToGif);
            imageFrameAddToGif.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
