package com.nichesoftware.giftlist.presenters;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.utils.BusProvider;
import com.nichesoftware.giftlist.views.IView;

/**
 * Base presenter
 */
public abstract class BasePresenter<V extends IView> implements IPresenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = BasePresenter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * View
     */
    protected V mAttachedView;
    /**
     * Data provider
     */
    protected DataProvider mDataProvider;

    /**
     * Constructor
     * @param view   View to attach
     * @param dataProvider   The data provider
     */
    public BasePresenter(@NonNull V view, @NonNull DataProvider dataProvider) {
        this.mDataProvider = dataProvider;
        this.mAttachedView = view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Lifecycle
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @CallSuper
    @Override
    public void onCreate() {
        Log.d(TAG, "[BasePresenter] onCreate");
        // Subscribing to the Bus event
        BusProvider.register(this);
    }

    @Override
    @CallSuper
    public void onDestroy() {
        Log.d(TAG, "[BasePresenter] onDestroy");
        // Unsubscribing to the Bus event
        BusProvider.unregister(this);
        // Dereferencing the view
        mAttachedView = null;
    }
}
