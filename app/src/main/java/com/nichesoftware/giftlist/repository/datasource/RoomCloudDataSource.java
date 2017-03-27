package com.nichesoftware.giftlist.repository.datasource;

import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.Service;

import java.util.List;

import io.reactivex.Observable;

/**
 * {@link CloudDataSource} implementation for {@link Room}.
 */
public class RoomCloudDataSource extends ConnectedDataSource<Room> {

    /**
     * Public constructor
     *
     * @param token         {@link User} token
     * @param service       The bound {@link Service}
     */
    public RoomCloudDataSource(final String token, final Service service) {
        super(token, service);
    }

    @Override
    public Observable<Room> add(Room element) {
        return mService.createRoom(mToken, element);
    }

    @Override
    public Observable<Room> get(String id) {
        throw new UnsupportedOperationException("This operation does not exist...");
    }

    @Override
    public Observable<List<Room>> getAll() {
        return mService.getAllRooms(mToken);
    }

    @Override
    public Observable<Room> update(Room element) {
        throw new UnsupportedOperationException("This operation does not exist...");
    }

    @Override
    public Observable<List<Room>> delete(Room element) {
        return mService.leaveRoom(mToken, element);
    }
}
