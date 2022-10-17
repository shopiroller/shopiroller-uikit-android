package com.shopiroller.util;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shopiroller.R;

public class DialogUtil {

    /**
     * Show no connection information
     * and destroy activity
     *
     * @param activity
     */
    public static void showNoConnectionError(Activity activity) {
        new MaterialDialog.Builder(activity)
                .title(R.string.connection_required_error)
                .content(R.string.please_check_your_internet_connection)
                .cancelable(false)
                .positiveText(R.string.OK)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        activity.finish();
                    }
                })
                .show();
    }

    /**
     * Just show no connection information
     *
     * @param context
     */
    public static void showNoConnectionInfo(Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.connection_required_error)
                .content(R.string.please_check_your_internet_connection)
                .positiveText(R.string.OK)
                .show();
    }


    public static void showNoConnectionInfo(Context context, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(R.string.connection_required_error)
                .content(R.string.please_check_your_internet_connection)
                .positiveText(R.string.OK)
                .onPositive(callback)
                .show();
    }

}
