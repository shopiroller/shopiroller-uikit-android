package com.shopiroller.helpers;

import android.content.Context;

import com.shopiroller.SharedApplication;

public class UtilManager {

    private static NetworkHelper networkHelper;
    private static LocalizationHelper localizationHelper;

    private static void init(Context context) {
        networkHelper = new NetworkHelper(context);
        localizationHelper = new LocalizationHelper();
    }

    public static NetworkHelper networkHelper() {
        if (networkHelper == null)
            networkHelper = new NetworkHelper(SharedApplication.context);
        return networkHelper;
    }

    public static LocalizationHelper localizationHelper() {
        if (localizationHelper == null)
            localizationHelper = new LocalizationHelper();
        return localizationHelper;
    }
}
