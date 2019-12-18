package com.oneMap.module.common.utils;

import android.app.Activity;

import com.zhy.m.permission.MPermissions;

/**
 * 权限 全局
 */

public class Permission {
    private static final int REQUECT_CODE_SDCARD = 0;

    public boolean setPermission(Activity activity) {
        MPermissions.requestPermissions(activity, REQUECT_CODE_SDCARD,
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.MOUNT_UNMOUNT_FILESYSTEMS",
                "android.permission.READ_PHONE_STATE",
                "android.permission.VIBRATE",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.CHANGE_WIFI_STATE",
                "android.permission.INTERNET",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.BAIDU_LOCATION_SERVICE",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_GPS",
                "android.permission.READ_LOGS",
                "android.permission.CAMERA",
                "android.permission.WRITE_SETTINGS");
        return true;
    }
}
