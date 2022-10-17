package com.shopiroller.helpers;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.shopiroller.R;
import com.shopiroller.Shopiroller;

/**
 * Created by ealtaca on 26.05.2017.
 */

public class LegacyToolbarHelper {

    public LegacyToolbarHelper() {
    }

    /**
     * Build toolbar object
     *
     * @param toolbar
     * @return
     */
    public static Toolbar setToolbar(final AppCompatActivity appCompatActivity, Toolbar toolbar) {
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.getNavigationIcon().setAutoMirrored(true);
        }
//        toolbar.setTitleTextColor(ContextCompat.getColor(appCompatActivity, R.color.white));
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appCompatActivity.finish();
                try {
                    View viewa = appCompatActivity.getCurrentFocus();
                    if (viewa != null) {
                        InputMethodManager imm = (InputMethodManager) appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(viewa.getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            appCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            appCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            appCompatActivity.getWindow().setStatusBarColor(getStatusBarColor());
        }
        return toolbar;
    }

    /**
     * Build toolbar object
     *
     * @param toolbar
     * @return
     */
    private Toolbar setToolbarPermission(final AppCompatActivity appCompatActivity, Toolbar toolbar) {
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#517fff"));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(ContextCompat.getColor(appCompatActivity, R.color.white));
        toolbar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.getNavigationIcon().setAutoMirrored(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appCompatActivity.finish();
            }
        });
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            appCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            appCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            appCompatActivity.getWindow().setStatusBarColor(getStatusBarColorPermission());
        }
        return toolbar;
    }

    /**
     * Set toolbar and related title for activity
     *
     * @param title
     */
    public void setToolbarTitle(AppCompatActivity appCompatActivity, String title) {
        try {
            setToolbar(appCompatActivity, appCompatActivity.findViewById(R.id.toolbar_top));
            appCompatActivity.getSupportActionBar().setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            // Toolbar set error
        }
    }

    /**
     * Set toolbar and related title for activity
     *
     * @param title
     */
    public void setToolbarTitlePermission(AppCompatActivity appCompatActivity, String title) {
        try {
            setToolbarPermission(appCompatActivity, appCompatActivity.findViewById(R.id.toolbar_top));
            appCompatActivity.getSupportActionBar().setTitle(title);
        } catch (Exception e) {
            // Toolbar set error
        }
    }

    public static int getStatusBarColor() {
        int color = Shopiroller.getTheme().primaryColor;
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * 0.9f);
        int g = Math.round(Color.green(color) * 0.9f);
        int b = Math.round(Color.blue(color) * 0.9f);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public static int getCustomStatusBarColor(int color) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * 0.9f);
        int g = Math.round(Color.green(color) * 0.9f);
        int b = Math.round(Color.blue(color) * 0.9f);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public static int getStatusBarColor(Context context) {
        int color = Shopiroller.getAdapter().getActionBarColor();
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * 0.9f);
        int g = Math.round(Color.green(color) * 0.9f);
        int b = Math.round(Color.blue(color) * 0.9f);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    private int getStatusBarColorPermission() {
        int color = Color.parseColor("#517fff");
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * 0.9f);
        int g = Math.round(Color.green(color) * 0.9f);
        int b = Math.round(Color.blue(color) * 0.9f);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public void setStatusBar(AppCompatActivity appCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = appCompatActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getStatusBarColor());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = appCompatActivity.getWindow().getDecorView();
            if ((ColorHelper.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK) == Color.WHITE) {
                int flags = decor.getSystemUiVisibility();
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                decor.setSystemUiVisibility(flags);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public void setStatusBar(AppCompatActivity appCompatActivity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = appCompatActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getCustomStatusBarColor(color));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = appCompatActivity.getWindow().getDecorView();
            if ((ColorHelper.isColorDark(getCustomStatusBarColor(color)) ? Color.WHITE : Color.BLACK) == Color.WHITE) {
                int flags = decor.getSystemUiVisibility();
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                decor.setSystemUiVisibility(flags);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public void setStatusBarColor(AppCompatActivity appCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = appCompatActivity.getWindow();
            window.setStatusBarColor(getStatusBarColor());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = appCompatActivity.getWindow().getDecorView();
            if ((ColorHelper.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK) == Color.WHITE) {
                int flags = decor.getSystemUiVisibility();
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                decor.setSystemUiVisibility(flags);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }


    public void setStatusBarTransparent(AppCompatActivity appCompatActivity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
