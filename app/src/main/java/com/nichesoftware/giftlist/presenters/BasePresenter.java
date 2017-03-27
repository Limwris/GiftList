package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.ConnectedDataSource;
import com.nichesoftware.giftlist.repository.datasource.DataSource;
import com.nichesoftware.giftlist.repository.provider.ConnectedDataSourceProvider;

/**
 * Base presenter
 */
public abstract class BasePresenter<V extends AuthenticationContract.View, M>
        extends AuthenticationPresenter<V> {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = BasePresenter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Data provider
     */
    protected final DataSource<M> mDataProvider;
    /**
     * Cache
     */
    protected final Cache<M> mCache;
    /**
     *
     */
    protected final ConnectedDataSource<M> mConnectedDataSource;

    /**
     * Constructor
     *
     * @param view                  View to attach
     * @param cache                 Cache
     * @param connectedDataSource   The cloud data provider
     * @param authDataSource        Authentication data source
     */
    public BasePresenter(@NonNull V view, @NonNull Cache<M> cache,
                         @NonNull ConnectedDataSource<M> connectedDataSource,
                         @NonNull AuthDataSource authDataSource) {
        super(view, authDataSource);
        mCache = cache;
        mConnectedDataSource = connectedDataSource;
        mDataProvider = new ConnectedDataSourceProvider<>(cache, connectedDataSource);
    }
}
