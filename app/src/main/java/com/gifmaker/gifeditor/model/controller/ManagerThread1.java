package com.gifmaker.gifeditor.model.controller;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by longdg on 2017-09-07.
 */

public class ManagerThread1 {
    public static ManagerThread1 managerThread1;
    private Handler handler;
    private ExecutorService mExecutorService;
    private ManagerThread1 (){
        init();
    }
    private void init() {
        if(mExecutorService == null || mExecutorService.isShutdown() || mExecutorService.isTerminated()) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        handler = new Handler(Looper.myLooper());
    }
    public static ManagerThread1 getInstance () {
        if (managerThread1 == null) {
            managerThread1 = new ManagerThread1();
        }
        return managerThread1;
    }
    public void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
    public void runOtherThread (Runnable runnable) {
        mExecutorService.submit(runnable);
    }
}
