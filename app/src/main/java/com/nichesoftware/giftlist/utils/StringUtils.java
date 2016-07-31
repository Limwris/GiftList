package com.nichesoftware.giftlist.utils;

/**
 * Created by n_che on 09/06/2016.
 */
public final class StringUtils {
    /**
     * Empty string
     */
    private static final String EMPTY_STRING = "";

    /**
     * Constructeur privé
     */
    private StringUtils() {
        // Nothing
    }

    /**
     * Return true if the string is empty, false otherwise
     * @param string
     * @return true si le string est null ou vide
     */
    public static boolean isEmpty(final String string) {
        return (string == null) || (EMPTY_STRING.equals(string)) || (string.length() < 1);
    }
}
