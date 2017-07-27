package com.nichesoftware.giftlist.repository.cache;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.model.Gift;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Observable;

/**
 * Implementation of {@link Cache} for {@link Gift}
 */
public class GiftCache implements Cache<Gift> {

    /**
     * Database manager
     */
    private final DatabaseManager mDatabaseManager;
    /**
     * {@link com.nichesoftware.giftlist.model.Room} primary key
     */
    private final String mRoomId;

    /**
     * Default constructor
     *
     * @param databaseManager Instance of {@link DatabaseManager}
     * @param roomId          {@link com.nichesoftware.giftlist.model.Room} primary key
     */
    public GiftCache(@NonNull DatabaseManager databaseManager, @NonNull final String roomId) {
        mDatabaseManager = databaseManager;
        mRoomId = roomId;
    }

    @Override
    public Observable<List<Gift>> getAll() {
        List<Gift> rooms = new ArrayList<>();
        rooms.addAll(mDatabaseManager.getAllGifts(mRoomId));

        return Observable.just(rooms);
    }

    @Override
    public Observable<Gift> get(String id) {
        Gift gift = mDatabaseManager.getGift(id);

        if (gift == null) {
            return Observable.error(new NoSuchElementException());
        } else {
            return Observable.just(gift);
        }
    }

    @Override
    public void put(Gift element) {
        mDatabaseManager.addOrUpdateGift(element, mRoomId);
    }

    @Override
    public void putAll(List<Gift> elements) {
        for (Gift gift : elements) {
            put(gift);
        }
        CacheTimer.setLastCacheUpdateTimeMillis(CacheTimer.SETTINGS_GIFT_KEY_LAST_CACHE_UPDATE);
    }

    @Override
    public void evictAll() {
        CacheTimer.setCacheExpired(CacheTimer.SETTINGS_GIFT_KEY_LAST_CACHE_UPDATE);
        mDatabaseManager.deleteGifts(mRoomId);
    }

    @Override
    public boolean isCached(String id) {
        return mDatabaseManager.giftExists(id);
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = CacheTimer.getLastCacheUpdateTimeMillis(CacheTimer.SETTINGS_GIFT_KEY_LAST_CACHE_UPDATE);

        boolean expired = ((currentTime - lastUpdateTime) > CacheTimer.EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }
}
