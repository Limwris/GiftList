package com.nichesoftware.giftlist.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;

import java.util.List;

/**
 * Created by n_che on 15/04/2016.
 */
public final class AndroidUtils {
    private static final String TAG = AndroidUtils.class.getSimpleName();

    /**
     * Private constructor
     */
    private AndroidUtils() {
        // Nothing
    }

    /**
     * Method checks if the app is in background or not
     */
    @SuppressWarnings("deprecation")
    public static boolean isAppInBackground(Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "isAppInBackground");
        }
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
