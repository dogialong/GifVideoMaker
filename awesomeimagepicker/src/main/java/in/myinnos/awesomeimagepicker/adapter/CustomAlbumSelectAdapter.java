package in.myinnos.awesomeimagepicker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.R;
import in.myinnos.awesomeimagepicker.models.Album;

/**
 * Created by MyInnos on 03-11-2016.
 */
public class CustomAlbumSelectAdapter extends RecyclerView.Adapter<CustomAlbumSelectAdapter.ViewHolder> {
    private int marginRight = 10;
    protected int size;
    private Context context;

    private ArrayList<Album> albums;
    private OnItemClick onitemclick;

    public CustomAlbumSelectAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_view_item_album_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (albums.get(position).name.equals("Take Photo")) {

            Glide.with(context).load(albums.get(position).cover)
                    .placeholder(R.color.colorAccent)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .crossFade()
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(100,100)
                    .centerCrop()
                    .into(holder.overlayImage);
        } else {
            final Uri uri = Uri.fromFile(new File(albums.get(position).cover));
            Glide.with(context).load(uri)
                    .placeholder(Color.BLACK)
                    .thumbnail(0.1f)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(100,100)
                    .centerCrop()
                    .into(holder.overlayImage);
        }
        holder.textView.setText(albums.get(position).name);
    }

    @Override
    public int getItemCount() {
        return albums.size();

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.imageView);
        Glide.clear(holder.overlayImage);
        if(holder.imageView != null) {
            holder.imageView = null;
        }
        if (holder.overlayImage!=null) {
            holder.overlayImage = null;
        }
        if (holder.root!=null) {
            holder.root = null;
        }
    }

    public void setLayoutParams(int size) {
        this.size = size;
    }
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        Glide.clear(holder.imageView);
        Glide.clear(holder.overlayImage);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        RelativeLayout root;
        ImageView overlayImage;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_album_image);
            textView = (TextView) itemView.findViewById(R.id.text_view_album_name);
            root = (RelativeLayout) itemView.findViewById(R.id.frame_layout_album_select_linh);
            RelativeLayout.LayoutParams layoutParamsOverlay = new RelativeLayout.LayoutParams(size * 222 / 242 - marginRight, size * 219 / 235 - marginRight);
            RelativeLayout.LayoutParams layoutParamsImageviewBackground = new RelativeLayout.LayoutParams(size - marginRight, size - marginRight);
            layoutParamsOverlay.setMargins(5, 5, 0, 70);
            layoutParamsImageviewBackground.setMargins(0, 0, marginRight, 0);
            overlayImage = new ImageView(context);
            overlayImage.setLayoutParams(layoutParamsOverlay);
            imageView.setLayoutParams(layoutParamsImageviewBackground);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            root.addView(overlayImage);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onitemclick != null)
                onitemclick.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onitemclick = onItemClick;
    }

}
