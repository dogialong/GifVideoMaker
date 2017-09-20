package com.gifmaker.gifeditor.ui.activity;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.gifmaker.gifeditor.utils.MemoryUtils;
import com.gifmaker.gifeditor.utils.PermissionUtils;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private  PermissionUtils.OnRequestPermissionsResult mOnRequestPermissionsResult;

    public void setOnRequestPermissionsResult(PermissionUtils.OnRequestPermissionsResult onRequestPermissionsResult){
        this.mOnRequestPermissionsResult = onRequestPermissionsResult;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.

            if(mOnRequestPermissionsResult!=null){
                mOnRequestPermissionsResult.permissionAccept(requestCode);
            }

        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.

            if(mOnRequestPermissionsResult!=null){
                mOnRequestPermissionsResult.permissionCancel(requestCode);
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemoryUtils.releaseMemoryByGlide(this);
    }
}
