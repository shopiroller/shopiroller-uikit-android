package com.shopiroller.views.legacy;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shopiroller.R;
import com.shopiroller.enums.ColorEnum;
import com.shopiroller.util.ColorUtil;

public class ShopirollerFloatingActionButton extends FloatingActionButton {

    private int themeColor;

    public ShopirollerFloatingActionButton(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShopirollerFloatingActionButton, 0, 0);
        try {
            themeColor = a.getInteger(R.styleable.ShopirollerFloatingActionButton_sFABColorType, -1);
        } finally {

            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {

        if(themeColor != -1) {
            setBackgroundTintList(ColorStateList.valueOf(ColorEnum.getResIdByResOrder(themeColor)));
            Drawable drawable = getDrawable().getConstantState().newDrawable();
            drawable.mutate().setColorFilter(ColorUtil.isColorDark(ColorEnum.getResIdByResOrder(themeColor)) ? Color.WHITE : Color.BLACK, PorterDuff.Mode.MULTIPLY);
            setImageDrawable(drawable);
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(enabled){
            setBackgroundTintList(ColorStateList.valueOf(ColorEnum.getResIdByResOrder(themeColor)));
        } else
            setBackgroundTintList(ColorStateList.valueOf(ColorUtil.getColorWithAlpha(ColorEnum.getResIdByResOrder(themeColor),0.4f)));
    }
}
