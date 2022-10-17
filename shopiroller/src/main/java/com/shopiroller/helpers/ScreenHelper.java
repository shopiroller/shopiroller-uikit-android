package com.shopiroller.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenHelper {

    public static int getDeviceHeight(Activity appCompatActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int device_height = dm.heightPixels - getStatusBarHeight(appCompatActivity);
        return device_height;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            statusBarHeight = (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

}
