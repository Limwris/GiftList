package com.nichesoftware.giftlist;

import android.util.Log;

import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.ServiceAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class MockService implements ServiceAPI {
    private final static String TAG = MockService.class.getSimpleName();

    private static List<Room> ROOMS = new ArrayList<>();
    private static List<Gift> GIFTS = new ArrayList<>();

    static {
        Gift gift = new Gift();
        gift.setName("Ours en peluche");
        gift.setPrice(22.05);
        GIFTS.add(gift);

        gift = new Gift();
        gift.setName("Playstation 8");
        gift.setPrice(455.99);
        GIFTS.add(gift);

        ROOMS.add(new Room(0, "John Doe", "Anniversaire"));
        ROOMS.add(new Room(1, "Jane Doe", "Noël"));
        ROOMS.add(new Room(2, "Jean-Charles Dupond", "Pot de départ"));
    }

    @Override
    public void authenticate(String username, String password, ServiceCallback<String> callback) {
        callback.onLoaded("");
    }

    @Override
    public void register(String username, String password, String phoneNumber, ServiceCallback<String> callback) {
        callback.onLoaded("");
    }

    @Override
    public void getAllRooms(final String token, ServiceCallback<List<Room>> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getAllRooms [token = %s]", token));
        }
        callback.onLoaded(ROOMS);
    }

    @Override
    public void getGifts(String token, int roomId, ServiceCallback<List<Gift>> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getGifts [token = %s, roomId = %d]", token, roomId));
        }
        callback.onLoaded(GIFTS);
    }

    @Override
    public void createRoom(String token, String roomName, String occasion, ServiceCallback<Room> callback) {
        callback.onLoaded(new Room(0, "Test", "Test"));
    }

    @Override
    public void addGift(String token, int roomId, String name, double price, double amount, ServiceCallback<Gift> callback) {
        Gift gift = new Gift();
        gift.setName("PS4");
        gift.setPrice(250);
        callback.onLoaded(gift);

    }

    @Override
    public void updateGift(String token, int roomId, int giftId, double amount, ServiceCallback<Gift> callback) {
        Gift gift = new Gift();
        gift.setName("PS4");
        gift.setPrice(250);
        callback.onLoaded(gift);
    }

    @Override
    public void retreiveAvailableUsers(final String token, final int roomId,
                                       final List<String> phoneNumber,
                                       ServiceCallback<List<User>> callback) {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("User1");
        users.add(user1);
        User user2 = new User();
        user2.setUsername("User2");
        users.add(user2);
        callback.onLoaded(users);
    }

    @Override
    public void inviteUserToRoom(String token, int roomId, String username, ServiceCallback<Boolean> callback) {
        callback.onError();
    }
}
