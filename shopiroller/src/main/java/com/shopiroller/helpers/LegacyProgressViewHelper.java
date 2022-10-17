package com.shopiroller.helpers;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.shopiroller.Shopiroller;

/**
 * Created by ealtaca on 27.12.2016.
 */

public class LegacyProgressViewHelper {

    private ProgressViewHelper progressViewHelper;

    public LegacyProgressViewHelper(AppCompatActivity context) {
        init(context);
    }

    public LegacyProgressViewHelper(FragmentActivity context) {
        init(context);
    }

    public LegacyProgressViewHelper(Activity context) {
        init(context);
    }

    private void init(Activity activity) {
        progressViewHelper = new ProgressViewHelper(activity,
                Shopiroller.getTheme().progressType,
                Shopiroller.getTheme().progressColor);
    }

    public void show() {
        progressViewHelper.show();
    }

    public void dismiss() {
        progressViewHelper.dismiss();
    }

    public void cancel() {
        progressViewHelper.cancel();
    }

    public ProgressBar getProgressDialog() {
        return progressViewHelper.getProgressDialog();
    }

    public void setNotCancelable() {
        progressViewHelper.notCancelable();
    }

    public boolean isShowing() {
        return progressViewHelper.isShowing();
    }

    public Drawable getProgressDrawable() {
        return progressViewHelper.getProgressDrawable();
    }

}
