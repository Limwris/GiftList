package com.nichesoftware.giftlist.repository.cache;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Observable;

/**
 * Implementation of {@link Cache} for {@link Room}
 */
public class RoomCache implements Cache<Room> {

    /**
     * Database manager
     */
    private final DatabaseManager mDatabaseManager;
    /**
     * Username ({@link User} primary key)
     */
    private final String mUserId;

    /**
     *  Default constructor
     *
     *  @param databaseManager      Instance of {@link DatabaseManager}
     *  @param username             {@link User} primary key
     */
    public RoomCache(@NonNull DatabaseManager databaseManager, @NonNull final String username) {
        mDatabaseManager = databaseManager;
        mUserId = username;
    }

    @Override
    public Observable<List<Room>> getAll() {
        List<Room> rooms = new ArrayList<>();
        rooms.addAll(mDatabaseManager.getAllRooms(mUserId));

        return Observable.just(rooms);
    }

    @Override
    public Observable<Room> get(String id) {
        Room room = mDatabaseManager.getRoom(id);

        if (room == null) {
            return Observable.error(new NoSuchElementException());
        } else {
            return Observable.just(room);
        }
    }

    @Override
    public void put(Room element) {
        mDatabaseManager.addOrUpdateRoom(element, mUserId);
    }

    @Override
    public void putAll(List<Room> elements) {
        for (Room room : elements) {
            put(room);
        }
        CacheTimer.setLastCacheUpdateTimeMillis(CacheTimer.SETTINGS_ROOM_KEY_LAST_CACHE_UPDATE);
    }

    @Override
    public void evictAll() {
        CacheTimer.setCacheExpired(CacheTimer.SETTINGS_ROOM_KEY_LAST_CACHE_UPDATE);
        mDatabaseManager.deleteRooms(mUserId);
    }

    @Override
    public boolean isCached(String id) {
        return mDatabaseManager.roomExists(id);
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = CacheTimer.getLastCacheUpdateTimeMillis(CacheTimer.SETTINGS_ROOM_KEY_LAST_CACHE_UPDATE);

        boolean expired = ((currentTime - lastUpdateTime) > CacheTimer.EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }
}
