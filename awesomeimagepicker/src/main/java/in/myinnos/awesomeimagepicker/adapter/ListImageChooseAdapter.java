package in.myinnos.awesomeimagepicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.R;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class ListImageChooseAdapter extends RecyclerView.Adapter<ListImageChooseAdapter.ViewHolder> {

    private ArrayList<Image> listImage;
    private OnItemClickListener mItemClickListener;
    private Context context;
    public ListImageChooseAdapter(Context context,ArrayList<Image> listImage) {
        this.listImage = listImage;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_image_choose, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri uri = Uri.fromFile(new File(listImage.get(position).path));
        Glide.with(context)
                .load(uri)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(200, 200)
                .crossFade()
                .dontAnimate()
                .thumbnail(0.5f)
                .centerCrop()
                .into(holder.image);
    }
    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.image);
    }
    public void clearGlide () {
        Glide.get(context).clearMemory();
    }
    @Override
    public int getItemCount() {
        if(listImage!=null)
            return listImage.size();
        return 0;
    }
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        Glide.clear(holder.image);
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            image.setOnClickListener(this);
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
