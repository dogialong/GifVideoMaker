package com.gifmaker.gifeditor.ui.method;

import android.content.Context;
import android.graphics.Typeface;

import com.gifmaker.gifeditor.model.ItemFontText;

import java.util.ArrayList;

/**
 * Created by PhungVanQuang on 8/7/2017.
 */

public class ListFontText {
//    public static ArrayList<String> namefonts = new ArrayList<>();


    public static ArrayList<Typeface> getListTypeFace(Context context) {
        ArrayList<Typeface> listFont = new ArrayList<>();
        listFont.add(null);
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/comici.ttf"));
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/segoepr.ttf"));
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/SFUGaramondCondensedBold.TTF"));
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/SFUJamaicaRegular.TTF"));
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/UTM Alba Matter.ttf"));
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/UTM Brushtip-C.ttf"));
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/UTM Chickenhawk.ttf"));
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/UTM Novido.ttf"));
        listFont.add(Typeface.createFromAsset(context.getAssets(), "fonts/UTM Tam Le.ttf"));
        return listFont;
    }

    public static ArrayList<ItemFontText> getListItemFontText(Context context) {
        ArrayList<ItemFontText> itemFontTexts = new ArrayList<>();
        ItemFontText itemFontText1;
          ArrayList<String> namefonts = new ArrayList<>();
        namefonts.add("Default");
        namefonts.add("ComicSansMS-Italic");
        namefonts.add("SFUGaramond Condensed Bold");
        namefonts.add("SFUJAMAICA");
        namefonts.add("UTMAlba-Matter");
        namefonts.add("UTM Brushtip-C");
        namefonts.add("UTM Chickenhawk");
        namefonts.add("UTM Novido");
        namefonts.add("UTM Tam Le");
     for(int  i= 0 ; i< namefonts.size();i++){
         itemFontText1 = new ItemFontText(namefonts.get(i));
         itemFontTexts.add(itemFontText1);
     }
        return itemFontTexts;


    }

}
