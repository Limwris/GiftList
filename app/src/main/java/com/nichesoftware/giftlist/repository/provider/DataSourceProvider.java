package com.nichesoftware.giftlist.repository.provider;

import android.util.Log;

import com.nichesoftware.giftlist.BaseApplication;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSource;
import com.nichesoftware.giftlist.repository.datasource.DataSource;
import com.nichesoftware.giftlist.repository.datasource.DiskDataSource;
import com.nichesoftware.giftlist.utils.NetworkUtils;

import java.net.ConnectException;
import java.util.List;

import io.reactivex.Observable;

/**
 * Factory that creates different implementations of {@link DataSource} for {@link CloudDataSource}.
 */
public class DataSourceProvider<T> implements DataSource<T> {
    // Constants
    private static final String TAG = DataSourceProvider.class.getSimpleName();

    // Fields
    /**
     * {@link Cache}
     */
    private final Cache<T> mCache;
    /**
     * {@link DiskDataSource}
     */
    private final DiskDataSource<T> mDiskDataSource;
    /**
     * {@link CloudDataSource}
     */
    private final CloudDataSource<T> mCloudDataSource;

    /**
     * Default constructor
     *
     * @param cache           {@link Cache}
     * @param cloudDataSource {@link CloudDataSource}
     */
    public DataSourceProvider(Cache<T> cache, CloudDataSource<T> cloudDataSource) {
        mCache = cache;
        mDiskDataSource = new DiskDataSource<>(cache);
        mCloudDataSource = cloudDataSource;
    }

    @Override
    public Observable<T> add(T element) {
        if (!NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return Observable.error(new ConnectException());
        } else {
            return mCloudDataSource.add(element).doOnNext(mCache::put);
        }
    }

    @Override
    public Observable<List<T>> getAll() {
        if (!mCache.isExpired()) {
            Log.d(TAG, "getAll: from cache");
            return mDiskDataSource.getAll()
                    .onErrorResumeNext(mCloudDataSource.getAll().doOnNext(mCache::putAll));
        } else if (!NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return Observable.error(new ConnectException("No network connection"));
        } else {
            Log.d(TAG, "getAll: from cloud");
            // If the cache is expired, refresh it
            return mCloudDataSource.getAll().doOnNext(mCache::putAll);
        }
    }

    @Override
    public Observable<T> get(String id) {
        if (!mCache.isExpired() && mCache.isCached(id)) {
            Log.d(TAG, "get: from cache");
            // If operation on disk datasource failed, call the cloud datasource
            return mDiskDataSource.get(id)
                    .onErrorResumeNext(mCloudDataSource.get(id).doOnNext(mCache::put));
        } else if (!NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return Observable.error(new ConnectException("No network connection"));
        } else {
            Log.d(TAG, "get: from cloud");
            // If the cache is expired, refresh it
            return mCloudDataSource.get(id).doOnNext(mCache::put);
        }
    }

    @Override
    public Observable<T> update(T element) {
        if (!NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return Observable.error(new ConnectException());
        } else {
            return mCloudDataSource.update(element).doOnNext(mCache::put);
        }
    }

    @Override
    public Observable<List<T>> delete(T element) {
        if (!NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return Observable.error(new ConnectException());
        } else {
            return mCloudDataSource.delete(element).doOnNext(elements -> {
                mCache.evictAll();
                mCache.putAll(elements);
            });
        }
    }
}
