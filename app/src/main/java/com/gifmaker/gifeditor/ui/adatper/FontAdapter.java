package com.gifmaker.gifeditor.ui.adatper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.model.ItemFontText;

import java.util.ArrayList;

/**
 * Created by longdg on 2017-08-03.
 */

public class FontAdapter extends  RecyclerView.Adapter<FontAdapter.ViewHolder> {
    private ArrayList<ItemFontText> itemFontTexts;
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Typeface> typefaces;
    private IListClickListener iListClickListener;
    public FontAdapter(Context mContext,ArrayList<ItemFontText> itemFontTexts ,ArrayList<Typeface> typefaces, IListClickListener iListClickListener) {
        this.itemFontTexts = itemFontTexts;
        this.mContext = mContext;
        this.iListClickListener = iListClickListener;
        this.typefaces = typefaces;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View  view = inflater.inflate(R.layout.item_list_font, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Typeface typeface  = typefaces.get(position);
        ItemFontText itemFontText = itemFontTexts.get(position);
        holder.itemFont.setText(itemFontText.getNameFont());
        holder.itemFont.setTypeface(typeface);
        holder.itemFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListClickListener.onClickListener(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemFontTexts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemFont;


        public ViewHolder(View itemView) {
            super(itemView);
            itemFont = (TextView) itemView.findViewById(R.id.tv_font);
        }

    }
    public interface IListClickListener {
        void onClickListener(View view, int position);
    } ;

}

