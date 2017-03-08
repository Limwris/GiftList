package com.nichesoftware.giftlist.views;

import android.support.annotation.NonNull;

/**
 * Base view contract
 */
public interface IView {
    /**
     * Displays loading view with message
     */
    void showLoader();

    /**
     * Hides loading view
     */
    void hideLoader();

    /**
     * Displays a specific message to the current screen
     * @param message the message
     */
    void showError(@NonNull String message);
}
