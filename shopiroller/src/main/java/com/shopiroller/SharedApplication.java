package com.shopiroller;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LifecycleObserver;
import androidx.multidex.MultiDexApplication;

public abstract class SharedApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = SharedApplication.this;
    }

}
