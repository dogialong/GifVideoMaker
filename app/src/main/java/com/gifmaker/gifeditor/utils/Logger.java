package com.gifmaker.gifeditor.utils;

import android.util.Log;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class Logger {

    public static void e(String tag, String msg){
        if(ConfigApp.DEBUG)
         Log.e(tag,msg);
    }
    public static  void d(String tag, String msg){
        if(ConfigApp.DEBUG)
            Log.d(tag,msg);
    }
    public static void w(String tag, String msg){
        if(ConfigApp.DEBUG)
            Log.w(tag,msg);
    }
    public static void v(String tag, String msg){
        if(ConfigApp.DEBUG)
            Log.v(tag,msg);
    }

}
