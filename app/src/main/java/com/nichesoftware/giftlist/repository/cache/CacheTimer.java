package com.nichesoftware.giftlist.repository.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.nichesoftware.giftlist.BaseApplication;

/**
 * Timer which handles cache expiration
 */
/* package */ final class CacheTimer {
    // Constants
    private static final String SETTINGS_FILE_NAME = "com.nichesoftware.giftlist.SETTINGS";
    /* package */ static final String SETTINGS_USER_KEY_LAST_CACHE_UPDATE = "last_cache_update";
    /* package */ static final String SETTINGS_ROOM_KEY_LAST_CACHE_UPDATE = "last_cache_update";
    /* package */ static final String SETTINGS_GIFT_KEY_LAST_CACHE_UPDATE = "last_cache_update";
    /* package */ static final long EXPIRATION_TIME = 60 * 10 * 1000;

    /**
     * Private constructor
     */
    private CacheTimer() {
        // Nothing
    }

    /**
     * Set in millis, the last time the cache was accessed.
     *
     * @param settingsKey
     */
    public static void setLastCacheUpdateTimeMillis(String settingsKey) {
        final Context context = BaseApplication.getAppContext();
        final long currentMillis = System.currentTimeMillis();
        writeToPreferences(context, SETTINGS_FILE_NAME, settingsKey, currentMillis);
    }

    /**
     * Indicates the cache is expired.
     *
     * @param settingsKey
     */
    public static void setCacheExpired(String settingsKey) {
        final Context context = BaseApplication.getAppContext();
        final long expiredMillis = System.currentTimeMillis() - EXPIRATION_TIME;
        writeToPreferences(context, SETTINGS_FILE_NAME, settingsKey, expiredMillis);
    }

    /**
     * Get in millis, the last time the cache was accessed.
     *
     * @param settingsKey
     */
    public static long getLastCacheUpdateTimeMillis(String settingsKey) {
        final Context context = BaseApplication.getAppContext();
        return getFromPreferences(context, SETTINGS_FILE_NAME, settingsKey);
    }

    /**
     * Write a value to a user preferences file.
     *
     * @param context            {@link android.content.Context} to retrieve android user preferences.
     * @param preferenceFileName A file name reprensenting where data will be written to.
     * @param key                A string for the key that will be used to retrieve the value in the future.
     * @param value              A long representing the value to be inserted.
     */
    private static void writeToPreferences(Context context, String preferenceFileName,
                                           String key, long value) {

        final SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName,
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * Get a value from a user preferences file.
     *
     * @param context            {@link android.content.Context} to retrieve android user preferences.
     * @param preferenceFileName A file name representing where data will be get from.
     * @param key                A key that will be used to retrieve the value from the preference file.
     * @return A long representing the value retrieved from the preferences file.
     */
    private static long getFromPreferences(Context context, String preferenceFileName, String key) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName,
                Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }
}
