package com.nichesoftware.giftlist.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Common navigation listener for view.
 */
public interface INavigationListener {

    /**
     * Moves to the activity given in parameter with bundle
     * @param activity the next activity class
     * @param bundle the Bundle
     */
    void performChangeActivity(@NonNull final Class<? extends BaseActivity> activity, @Nullable Bundle bundle);
}
