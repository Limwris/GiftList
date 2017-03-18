package com.nichesoftware.giftlist;

import android.app.Application;
import android.content.Context;

/**
 * Application class
 */
public class BaseApplication extends Application {

    private static BaseApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        if (INSTANCE == null) {
            BaseApplication.INSTANCE = this;
        }
    }

    public static Context getAppContext() {
        return INSTANCE.getApplicationContext();
    }

}
