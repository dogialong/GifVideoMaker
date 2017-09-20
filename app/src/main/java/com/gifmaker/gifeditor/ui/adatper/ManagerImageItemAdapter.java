package com.gifmaker.gifeditor.ui.adatper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.view.ItemTouchHelperAdapter;
import com.gifmaker.gifeditor.ui.view.ItemTouchHelperViewHolder;
import com.gifmaker.gifeditor.ui.view.OnStartDragListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.rey.material.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class ManagerImageItemAdapter extends RecyclerView.Adapter<ManagerImageItemAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Image> listImage;
    private ArrayList<Boolean> arrIsSelected;
    private View view;
    private OnItemClickListener mItemClickListener;
    private Context context;
    private final OnStartDragListener mDragStartListener;

    public ManagerImageItemAdapter(Context context, ArrayList<Image> listImage, ArrayList<Boolean> arrIsSelected
            , OnStartDragListener mDragStartListener) {
        this.listImage = listImage;
        this.arrIsSelected = arrIsSelected;
        this.context = context;
        this.mDragStartListener = mDragStartListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ImageSize imageSize = new ImageSize(100,100);
        ImageLoader.getInstance().displayImage("file://" +listImage.get(position).path,holder.imageToGif,imageSize);

           holder.imageToGif.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mDragStartListener != null) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });

    }


    @Override
    public int getItemCount() {
        if (listImage != null)
            return listImage.size();
        return 0;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(listImage, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        listImage.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
        ImageView imageToGif;
        ImageView imgDelete;
        RelativeLayout parentView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageToGif = (ImageView) itemView.findViewById(R.id.imageToGif);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDeleteItemManage);
            parentView = (RelativeLayout) itemView.findViewById(R.id.parentView);
            imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public void onItemSelected() {
            parentView.setBackgroundColor(Color.BLACK);
        }

        @Override
        public void onItemClear() {
            parentView.setBackgroundColor(0);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}

