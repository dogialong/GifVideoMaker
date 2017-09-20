package com.gifmaker.gifeditor.model.controller;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class ManagerThead {

    private static ManagerThead mManagerThead;
    private ExecutorService mExecutorService;
    private Handler mHandler;
    private ManagerThead(){
        init();
    }

    private void init(){
        if(mExecutorService == null || mExecutorService.isShutdown() || mExecutorService.isTerminated()){
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        mHandler = new Handler(Looper.myLooper());
    }

    public static ManagerThead getInstance(){
        if(mManagerThead == null)
            mManagerThead = new ManagerThead();

        return mManagerThead;
    }

    public void runOtherThead(Runnable runnable){

        mExecutorService.submit(runnable);

    }

    public void runUiThead(Runnable runnable){
        mHandler.post(runnable);
    }


}
