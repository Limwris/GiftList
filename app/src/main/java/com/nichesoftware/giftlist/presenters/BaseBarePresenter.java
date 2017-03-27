package com.nichesoftware.giftlist.presenters;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.views.IView;

/**
 * Bare base presenter
 */
/* package */ abstract class BaseBarePresenter<V extends IView> implements IPresenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = BasePresenter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * View
     */
    protected V mAttachedView;

    /**
     * Default constructor
     *
     * @param view                  View to attach
     */
    @SuppressWarnings("WeakerAccess")
    public BaseBarePresenter(@NonNull V view) {
        mAttachedView = view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Lifecycle
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @CallSuper
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    @CallSuper
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        // Dereferencing the view
        mAttachedView = null;
    }
}
