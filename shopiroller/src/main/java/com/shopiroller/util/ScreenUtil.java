package com.shopiroller.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shopiroller.constants.Constants;


/**
 * Created by ealtaca on 9/17/18.
 */

public class ScreenUtil {

    private static int screenWidth;
    private static int screenHeigth;

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static int getScreenWidth() {
        if (screenWidth == 0)
            screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenHeigth == 0)
            screenHeigth = Resources.getSystem().getDisplayMetrics().heightPixels;
        return screenHeigth;
    }

    public static int getWidthForDevice(int width) {
        int device_width = getScreenWidth();
        return Math.round((width * (device_width) / (Constants.MobiRoller_Preferences_StandardWidth)));
    }

    public static int getParamForColumns(int columns) {
        int deviceWidth = getScreenWidth();
        return Math.round((((Constants.MobiRoller_Preferences_StandardWidth - (columns * 10) - 20) / columns) * deviceWidth) / Constants.MobiRoller_Preferences_StandardWidth);
    }
}
