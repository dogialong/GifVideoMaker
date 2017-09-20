package com.gifmaker.gifeditor.ui.adatper;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.view.GPUImageFilterTools;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import pl.droidsonroids.gif.GifGPUImageView;

import static com.gifmaker.gifeditor.ui.view.GPUImageFilterTools.createFilterForType;

/**
 * Created by Ahmed Adel on 5/8/17.
 */

public class EffectImageGifAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private GPUImageFilterTools.FilterList filterList;
    private OnItemClickListener mItemClickListener;
    private Context context;
    private String[] listimage;

    public EffectImageGifAdapter(Context context,GPUImageFilterTools.FilterList filterList,String[] listimage) {
        this.filterList = filterList;
        this.context = context;
        this.listimage = listimage;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_gif_effect, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        int resourceId = context.getResources().getIdentifier(listimage[i], "drawable", context.getPackageName());
        Log.e("id",resourceId +" " + R.drawable.contrast);
        ViewHolder thumbnailsViewHolder = (ViewHolder) holder;
        thumbnailsViewHolder.nameEffect.setText(filterList.names.get(i));
        thumbnailsViewHolder.thumbnail.setBackgroundResource(resourceId);

    }

    @Override
    public int getItemCount() {
        return filterList.names.size();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public ImageView thumbnail;
        public TextView nameEffect;

        public ViewHolder(View v) {
            super(v);
            this.thumbnail = (ImageView) v.findViewById(R.id.thumbnaileffect);
            this.nameEffect = (TextView)v.findViewById(R.id.name_effects);
            this.thumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v,getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
