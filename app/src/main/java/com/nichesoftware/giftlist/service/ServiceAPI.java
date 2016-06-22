package com.nichesoftware.giftlist.service;

import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public interface ServiceAPI {
    interface ServiceCallback<T> {
        void onLoaded(T value);
        void onError();
    }

    void authenticate(final String username, final String password,
                      final ServiceCallback<String> callback);
    void register(final String username, final String password, final String phoneNumber,
                  final ServiceCallback<String> callback);
    void getAllRooms(final String token, final ServiceCallback<List<Room>> callback);
    void getGifts(final String token, final int roomId, final ServiceCallback<List<Gift>> callback);
    void createRoom(final String token, final String roomName,
                    final String occasion, final ServiceCallback<Room> callback);
    void addGift(final String token, int roomId, final String name, double price,
                 double amount, final ServiceCallback<Gift> callback);
    void updateGift(final String token, int roomId, int giftId,
                 double amount, final ServiceCallback<Gift> callback);
    void retreiveAvailableUsers(final String token, final List<String> phoneNumber,
                                final ServiceCallback<List<User>> callback);
}
