package com.shopiroller.views.legacy;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopiroller.Shopiroller;
import com.shopiroller.util.ColorUtil;

public class ShopirollerSwipeRefreshLayout extends SwipeRefreshLayout {

    public ShopirollerSwipeRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ShopirollerSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEnabled(false);
        setTheme();
    }

    private void setTheme() {
        setColorSchemeColors(
                ColorUtil.isColorDark(Shopiroller.getTheme().primaryColor) ? Shopiroller.getTheme().primaryColor : Color.BLACK);
    }

}
