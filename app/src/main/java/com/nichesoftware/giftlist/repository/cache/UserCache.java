package com.nichesoftware.giftlist.repository.cache;

import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.model.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * Implementation of {@link Cache} for {@link User}
 */
public final class UserCache implements Cache<User> {
    /**
     * Database manager
     */
    private final DatabaseManager mDatabaseManager;

    /**
     * Default constructor
     */
    public UserCache(DatabaseManager databaseManager) {
        mDatabaseManager = databaseManager;
    }

    @Override
    public Observable<List<User>> getAll() {
        return Observable.error(new UnsupportedOperationException());
    }

    @Override
    public Observable<User> get(String id) {
        return Observable.error(new UnsupportedOperationException());
    }

    @Override
    public void put(User element) {
        mDatabaseManager.addOrUpdateUser(element);
    }

    @Override
    public void putAll(List<User> elements) {
        for (User user : elements) {
            put(user);
        }
        CacheTimer.setLastCacheUpdateTimeMillis(CacheTimer.SETTINGS_USER_KEY_LAST_CACHE_UPDATE);
    }

    @Override
    public void evictAll() {
        CacheTimer.setCacheExpired(CacheTimer.SETTINGS_USER_KEY_LAST_CACHE_UPDATE);
        mDatabaseManager.deleteUsers();
    }

    @Override
    public boolean isCached(String id) {
        return mDatabaseManager.userExists(id);
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = CacheTimer.getLastCacheUpdateTimeMillis(CacheTimer.SETTINGS_USER_KEY_LAST_CACHE_UPDATE);

        boolean expired = ((currentTime - lastUpdateTime) > CacheTimer.EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }
}
