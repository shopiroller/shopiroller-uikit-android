package com.shopiroller.helpers;

import android.content.Context;

import com.shopiroller.Shopiroller;

/**
 * Created by ealtaca on 26.05.2017.
 */

public class LocalizationHelper {

    public static Context context;

    public LocalizationHelper() {
    }

    public static String getLocalizedTitle(String text) {
        if(text == null)
            return "";
        if(!text.contains("<"))
            return text;
        if(!text.startsWith("{<") && !text.endsWith(">}"))
            return text;

        String[] parts = text.split("<" +  Shopiroller.getAdapter().getLocale().toUpperCase() + ">");

        if (parts.length <= 1) {
            return "";
        }

        int position = parts.length - 2;

        if (position > 0)
            return parts[position];
        else
            return text;
    }

}
