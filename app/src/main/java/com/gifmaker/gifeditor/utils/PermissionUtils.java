package com.gifmaker.gifeditor.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.gifmaker.gifeditor.ui.activity.BaseActivity;

import java.util.ArrayList;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class PermissionUtils {

    public final static int REQESTCODE_READ_EXTERNAL_STORAGE = 21;
    public final static int REQESTCODE_WRITE_EXTERNAL_STORAGE = 22;
    public final static int REQUESTCODE_CAMERA = 1;


    public static interface OnRequestPermissionsResult {

        public void permissionAccept(int requestCode);

        public void permissionCancel(int requestCode);


    }

    public static boolean checkPermissionForExternalStorage(Context context) {
        return checkPermissionExist(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public static boolean checkPermissionExist(Context context, String permission) {
        try {
            int result =
                    ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void requestStoragePermission(Activity activity, String permission, int requestCode, OnRequestPermissionsResult onRequestPermissionsResult) {
        requestStoragePermission(activity, new String[]{permission}, requestCode, onRequestPermissionsResult);
    }


    public static void requestStoragePermission(Activity activity, String[] permissions, int requestCode, OnRequestPermissionsResult onRequestPermissionsResult) {

        ArrayList<String> argPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermissionExist(activity, permission)) {
                argPermissions.add(permission);
            }
        }

        if (argPermissions.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (activity instanceof BaseActivity) {
                    ((BaseActivity) activity).setOnRequestPermissionsResult(onRequestPermissionsResult);
                }
                activity.requestPermissions(argPermissions.toArray(new String[argPermissions.size()]),
                        requestCode);
            } else {
                if (onRequestPermissionsResult != null)
                    onRequestPermissionsResult.permissionCancel(requestCode);
            }

        } else {
            if (onRequestPermissionsResult != null)
                onRequestPermissionsResult.permissionAccept(requestCode);
        }

    }
}
