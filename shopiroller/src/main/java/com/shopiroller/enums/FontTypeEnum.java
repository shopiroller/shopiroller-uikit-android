package com.shopiroller.enums;

import android.content.Context;
import android.graphics.Typeface;

import com.shopiroller.Shopiroller;
import com.shopiroller.util.cache.FontCache;

public enum FontTypeEnum {

    HeadOne(Shopiroller.getTheme().headOneTypeface,24, 0),
    HeadTwo(Shopiroller.getTheme().headTwoTypeface, 18,1),
    HeadingOne(Shopiroller.getTheme().headingOneTypeface, 14,2),
    HeadingTwo(Shopiroller.getTheme().headingTwoTypeface, 14,3),
    Paragraph(Shopiroller.getTheme().paragraphTypeface, 14,4),
    Span(Shopiroller.getTheme().spanTypeface,12,5),
    Badge(Shopiroller.getTheme().headingOneTypeface,9,6),
    BadgeLarge(Shopiroller.getTheme().headingOneTypeface,10,7);

    private int resId;
    private float fontSize;
    private int resOrder;

    FontTypeEnum(int resId, float fontSize, int resOrder) {
        this.resId = resId;
        this.fontSize = fontSize;
        this.resOrder = resOrder;
    }

    public int getResId() {
        return resId;
    }

    public int getResOrder() {
        return resOrder;
    }

    public float getFontSize() {
        return fontSize;
    }

    public static Typeface getResIdByResOrder(int order, Context context) {
        for(FontTypeEnum e : FontTypeEnum.values()){
            if(order == e.resOrder) return FontCache.getTypeface(context, e.getResId());
        }
        return null;
    }

    public static float getFontSizeByResOrder(int order) {
        for(FontTypeEnum e : FontTypeEnum.values()){
            if(order == e.resOrder) return e.getFontSize();
        }
        return 14;
    }

}
