package com.nichesoftware.giftlist.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Network related utility class
 */
public final class NetworkUtils {
    /**
     * Private constructor
     */
    private NetworkUtils() {
        // Nothing
    }

    /**
     * Permits to know if the network is available or not
     *
     * @param context       Application {@link Context}
     * @return  {@link Boolean#TRUE} if the network is available, {@link Boolean#FALSE} otherwise
     */
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
