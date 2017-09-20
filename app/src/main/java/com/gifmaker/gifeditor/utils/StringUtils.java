package com.gifmaker.gifeditor.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by mr.logic on 7/27/2017.
 */

public class StringUtils {


    public static String format4Digit(int value){
        return formatDecimal(value,"0000");
    }
    public static String formatDecimal(int value,String partten){
        DecimalFormat decimalFormat = new DecimalFormat(partten);
        return decimalFormat.format(value);
    }
    public static boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    public static String loadJSONFromAsset(Context context, String nameJson) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(nameJson);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static int UCLN(int a, int b) {
        while (a != b) {
            if (a > b)
                a = a - b;
            else
                b = b - a;
        }
        return (a);
    }
}
