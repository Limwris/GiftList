package com.nichesoftware.giftlist.utils;

import android.support.annotation.StringRes;

import com.nichesoftware.giftlist.BaseApplication;

/**
 * Utility class for {@link android.content.res.Resources}
 */
public final class ResourcesUtils {
    /**
     * Private constructor
     */
    private ResourcesUtils() {
        // Nothing
    }

    public static String getString(@StringRes int res) {
        return BaseApplication.getAppContext().getString(res);
    }
}
