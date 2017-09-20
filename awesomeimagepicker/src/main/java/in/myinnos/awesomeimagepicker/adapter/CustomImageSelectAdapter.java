package in.myinnos.awesomeimagepicker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
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
 * Created by MyInnos on 03-11-2016.
 */
public class CustomImageSelectAdapter extends RecyclerView.Adapter<CustomImageSelectAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Image> imageArrayList;
    private int size;
    private OnItemClick itemClick;

    public CustomImageSelectAdapter(Context context,ArrayList<Image> listimage) {
        this.context = context;
        this.imageArrayList = listimage;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.grid_view_image_select,parent,false);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) rootView.getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        rootView.setLayoutParams(layoutParams);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Uri uri = Uri.fromFile(new File(imageArrayList.get(position).path));

        Glide.with(context).load(uri)
                .placeholder(Color.BLACK)
                .skipMemoryCache(false)
                .override(200, 200)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .thumbnail(0.5f)
                .centerCrop()
                .into(holder.imageView);
        if (imageArrayList.get(position).isSelected) {
            holder.view.setImageResource(R.drawable.ic_select_img_done);

        } else {
            holder.view.setImageResource(R.drawable.ic_select_img);
        }


    }
    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.imageView);
        Glide.clear(holder.view);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        Glide.clear(holder.imageView);
        Glide.clear(holder.view);
    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }
    public void setLayoutParams (int size) {
        this.size = size;
    }


    public   class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public ImageView view;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image_view_image_select);
            view = (ImageView)itemView.findViewById(R.id.view_alpha);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                 if(itemClick!= null)
                     itemClick.onItemClick(getAdapterPosition());
        }
    }
    public interface OnItemClick{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClick mItemClickListener) {
        this.itemClick = mItemClickListener;
    }

}
