package com.nichesoftware.giftlist.repository.provider;

import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSource;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSourceDecorator;
import com.nichesoftware.giftlist.repository.datasource.DataSource;
import com.nichesoftware.giftlist.repository.datasource.DiskDataSource;

import java.util.List;

import io.reactivex.Observable;

/**
 * Handling {@link Invitation} data
 */
public class InvitationDataSourceProvider implements DataSource<Invitation> {
    /// Fields
    /**
     * {@link Cache}
     */
    private final Cache<Room> mCache;

    /**
     * {@link CloudDataSource} decorated to fire Exception if network is not available
     */
    private final CloudDataSourceDecorator<Invitation> mCloudDataSource;

    /**
     * Default constructor
     *
     * @param cache           {@link Cache}
     * @param cloudDataSource {@link CloudDataSource}
     */
    public InvitationDataSourceProvider(Cache<Room> cache, CloudDataSource<Invitation> cloudDataSource) {
        mCache = cache;
        mCloudDataSource = new CloudDataSourceDecorator<>(cloudDataSource);
    }

    @Override
    public Observable<Invitation> add(Invitation element) {
        return mCloudDataSource.add(element);
    }

    @Override
    public Observable<List<Invitation>> getAll() {
        return mCloudDataSource.getAll();
    }

    @Override
    public Observable<Invitation> get(String id) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public Observable<Invitation> update(Invitation element) {
        return mCloudDataSource.update(element)
                .doOnNext(invitation -> mCache.put(invitation.getRoom()));
    }

    @Override
    public Observable<List<Invitation>> delete(Invitation element) {
        return mCloudDataSource.delete(element);
    }
}
