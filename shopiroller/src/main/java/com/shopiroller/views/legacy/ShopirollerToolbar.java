package com.shopiroller.views.legacy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import com.shopiroller.R;
import com.shopiroller.Shopiroller;
import com.shopiroller.util.ColorUtil;

public class ShopirollerToolbar extends Toolbar {

    private boolean isTransparent;

    public ShopirollerToolbar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShopirollerToolbar, 0, 0);
        try {
            isTransparent = a.getBoolean(R.styleable.ShopirollerToolbar_sToolbarIsTransparent, false);
        } finally {
            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        if (!isTransparent)
            setBackgroundColor(Shopiroller.getTheme().primaryColor);
        setTitleTextColor(ColorUtil.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK);
        setTitleTypeface();
        setContentInsetsAbsolute(0, getContentInsetRight());
        setContentInsetsRelative(0, getContentInsetEnd());
        setContentInsetStartWithNavigation(0);
    }

    public void setTitleTypeface() {
//        TextView toolbarTextView = ViewUtil.getTitleTextView(this);
//        if (toolbarTextView != null) {
//            toolbarTextView.setTypeface(FontTypeEnum.getResIdByResOrder(3));
//            toolbarTextView.setIncludeFontPadding(false);
//        }
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        super.setNavigationIcon(icon);
        setNavigationIconTint();
    }

    @Override
    public void setNavigationIcon(int resId) {
        super.setNavigationIcon(resId);
        setNavigationIconTint();
    }

    private void setNavigationIconTint() {
        if (getNavigationIcon() != null)
            DrawableCompat.setTint(
                    DrawableCompat.wrap(getNavigationIcon()),
                    ColorUtil.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK
            );

    }

    @SuppressLint("NewApi")
    @Override
    public void inflateMenu(@MenuRes int resId) {
        super.inflateMenu(resId);
        Menu menu = getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getIcon() != null)
                DrawableCompat.setTint(item.getIcon(), ColorUtil.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK);
        }
        Drawable drawable = getOverflowIcon();
        if (drawable != null && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {
            drawable.setTint(ColorUtil.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK);
            setOverflowIcon(drawable);
        }
    }
}
