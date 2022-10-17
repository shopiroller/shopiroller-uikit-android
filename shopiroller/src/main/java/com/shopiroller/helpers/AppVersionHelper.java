package com.shopiroller.helpers;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.shopiroller.SharedApplication;

public class AppVersionHelper {
    public static String getAppVersionName() {
        try {
            PackageInfo packageInfo = SharedApplication.context.getPackageManager()
                    .getPackageInfo(SharedApplication.context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
