package com.gifmaker.gifeditor.ui.adatper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gifmaker.gifeditor.R;

public abstract class ThumbGifFrameAdapter extends RecyclerView.Adapter<ThumbGifFrameAdapter.ViewHolder> {

    public abstract int getCountThumb();

    public abstract void onBinImageView(ImageView imageView, int position);

    private View view;
    private OnItemClickListener mItemClickListener;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_image_frame, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
     //   ImageLoaderPresenter.displayThumbImage(holder.image.getContext(),holder.image,getPathThumb(position));
        onBinImageView(holder.image,position);
    }

    @Override
    public int getItemCount() {
        return getCountThumb();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
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
