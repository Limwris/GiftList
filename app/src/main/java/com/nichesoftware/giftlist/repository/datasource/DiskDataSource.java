package com.nichesoftware.giftlist.repository.datasource;

import com.nichesoftware.giftlist.repository.cache.Cache;

import java.util.List;

import io.reactivex.Observable;

/**
 * {@link DataSource} implementation based on file system data store.
 */
public class DiskDataSource<T> implements DataSource<T> {
    /**
     * {@link Cache}
     */
    private final Cache<T> mCache;

    /**
     * Default constructor
     *
     * @param cache           {@link Cache}
     */
    public DiskDataSource(Cache<T> cache) {
        mCache = cache;
    }

    @Override
    public Observable<List<T>> getAll() {
        return mCache.getAll();
    }

    @Override
    public Observable<T> get(String id) {
        return mCache.get(id);
    }

    @Override
    public Observable<T> update(T element) {
        mCache.put(element);
        return Observable.create(emitter -> {
            emitter.onNext(element);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<List<T>> delete(T element) {
        return null;
    }

    @Override
    public Observable<T> add(T element) {
        mCache.put(element);
        return Observable.create(emitter -> {
            emitter.onNext(element);
            emitter.onComplete();
        });
    }
}
