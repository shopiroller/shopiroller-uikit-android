package com.shopiroller.util;

import android.content.Context;
import android.widget.Toast;

import com.shopiroller.R;
import com.shopiroller.helpers.NetworkHelper;

public class ErrorUtils {
    public static void showErrorToast(Context context)
    {
        if (!NetworkHelper.isConnected(context))
            Toast.makeText(context, R.string.please_check_your_internet_connection, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, R.string.common_error, Toast.LENGTH_SHORT).show();
    }
}
