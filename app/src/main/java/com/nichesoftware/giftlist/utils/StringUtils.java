package com.nichesoftware.giftlist.utils;

/**
 * Utility class which handles {@link String}
 */
public final class StringUtils {
    // Constants
    public static final String EMPTY_STRING = "";

    /**
     * Constructeur privé
     */
    private StringUtils() {
        // Nothing
    }

    /**
     * Return true if the string is empty, false otherwise
     *
     * @param string    {@link String} to process
     * @return true if the string is empty or null, false otherwise
     */
    public static boolean isEmpty(final String string) {
        return (string == null) || (EMPTY_STRING.equals(string)) || (string.length() < 1);
    }
}
