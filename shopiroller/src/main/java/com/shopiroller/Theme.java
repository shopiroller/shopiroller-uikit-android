package com.shopiroller;

import android.graphics.Color;

import com.shopiroller.helpers.ColorHelper;

public class Theme {

    public int primaryColor = Shopiroller.getAdapter().getActionBarColor();
    public int secondaryColor = Color.parseColor("#ffad01");
    public int darkColor = Color.parseColor("#0039D1");
    public int headerColor = Color.parseColor("#0045FF");
    public int textColor = Color.parseColor("#969fa2");
    public int paragraphColor = Color.parseColor("#455154");

    public int buttonPrimaryColor = Color.BLACK;
    public int textPrimaryColor = Color.BLACK;
    public int textSecondaryColor = Color.parseColor("#455154");
    public int imagePrimaryColor = Color.BLACK;

    public int headOneTypeface = R.font.poppins_bold;
    public int headTwoTypeface = R.font.poppins_bold;
    public int headingOneTypeface = R.font.poppins_bold;
    public int headingTwoTypeface = R.font.poppins_semibold;
    public int paragraphTypeface = R.font.poppins_regular;
    public int spanTypeface = R.font.poppins_regular;

    public int antiColor = ColorHelper.isColorDark(primaryColor) ? Color.WHITE : Color.BLACK;

    public int progressType = 8;
    public int progressColor = Color.GRAY;


}


