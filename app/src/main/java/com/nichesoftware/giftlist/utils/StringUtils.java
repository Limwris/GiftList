package com.nichesoftware.giftlist.utils;

/**
 * Created by n_che on 09/06/2016.
 */
public final class StringUtils {
    /**
     * Constructeur privé
     */
    private StringUtils() {
        // Nothing
    }

    public static boolean isEmpty(final String string) {
        return string == null || string.length() == 0;
    }
}
