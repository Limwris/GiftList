package com.nichesoftware.giftlist;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Mocked implementation of the {@link Service}
 */
/* package */ class MockRestService implements Service {
    // Fields
    private final BehaviorDelegate<Service> delegate;
    private static List<Room> ROOMS = new ArrayList<>();
    private static User USER = new User();

    static {
        List<Gift> giftsRoom0 = new ArrayList<>();
        Gift gift = new Gift();
        gift.setId(UUID.randomUUID().toString());
        gift.setName("Ours en peluche");
        gift.setPrice(22.05);
        gift.setAmount(12);
        gift.setRemainder(6.05);
        Map<String, Double> amountPerUserGift1Room0 = new HashMap<>();
        amountPerUserGift1Room0.put("Jean-Pierre", 4d);
        gift.setAmountByUser(amountPerUserGift1Room0);
        giftsRoom0.add(gift);

        gift = new Gift();
        gift.setId(UUID.randomUUID().toString());
        gift.setName("Playstation 8");
        gift.setPrice(455.99);
        gift.setAmount(40);
        gift.setRemainder(210.99);
        Map<String, Double> amountPerUserGift2Room0 = new HashMap<>();
        amountPerUserGift2Room0.put("Jean-Bernard", 120d);
        amountPerUserGift2Room0.put("Jean-Marie", 85d);
        gift.setAmountByUser(amountPerUserGift2Room0);
        giftsRoom0.add(gift);
        ROOMS.add(new Room("0", "John Doe", "Anniversaire", giftsRoom0));

        List<Gift> giftsRoom1 = new ArrayList<>();
        gift = new Gift();
        gift.setId(UUID.randomUUID().toString());
        gift.setName("Orange");
        gift.setPrice(0.95);
        gift.setAmount(0);
        gift.setRemainder(0);
        Map<String, Double> amountPerUserGift1Room1 = new HashMap<>();
        amountPerUserGift1Room1.put("Jean-François", 0.95d);
        gift.setAmountByUser(amountPerUserGift1Room1);
        giftsRoom1.add(gift);

        gift = new Gift();
        gift.setId(UUID.randomUUID().toString());
        gift.setName("Téléphone ARA");
        gift.setPrice(649);
        gift.setAmount(250);
        gift.setRemainder(222);
        Map<String, Double> amountPerUserGift2Room1 = new HashMap<>();
        amountPerUserGift2Room1.put("Jean-Charles", 135d);
        amountPerUserGift2Room1.put("Jean-Marc", 42d);
        gift.setAmountByUser(amountPerUserGift2Room1);
        giftsRoom1.add(gift);
        ROOMS.add(new Room("1", "Jane Doe", "Noël", giftsRoom1));

        List<Gift> giftsRoom2 = new ArrayList<>();
        gift = new Gift();
        gift.setId(UUID.randomUUID().toString());
        gift.setName("Casque BOSE QuietComfort 35");
        gift.setPrice(380.90);
        gift.setAmount(80);
        gift.setRemainder(110.20);
        Map<String, Double> amountPerUserGift1Room2 = new HashMap<>();
        amountPerUserGift1Room2.put("Jean-Bernard", 110.30d);
        amountPerUserGift1Room2.put("Jean-Pierre", 95.40d);
        gift.setAmountByUser(amountPerUserGift1Room2);
        giftsRoom2.add(gift);

        gift = new Gift();
        gift.setId(UUID.randomUUID().toString());
        gift.setName("Trilogie Fondation d'Isaac Azimov");
        gift.setPrice(49.50);
        gift.setAmount(25.50);
        gift.setRemainder(24);
        giftsRoom2.add(gift);
        ROOMS.add(new Room("2", "Jean-Charles Dupond", "Pot de départ", giftsRoom2));

        USER.setToken("token");
        USER.setName("Username");
        USER.setPhoneNumber("0102030405");
        USER.setRooms(ROOMS);
        USER.setIsTokenSent(true);
    }

    /* package */ MockRestService(MockRetrofit mockRetrofit) {
        delegate = mockRetrofit.create(Service.class);
    }

    @Override
    public Observable<User> authenticate(@Body User user) {
        return delegate.returningResponse(USER).authenticate(user);
    }

    @Override
    public Observable<User> register(@Body User user) {
        return delegate.returningResponse(USER).register(user);
    }

    @Override
    public Observable<List<Invitation>> getPendingInvitation() {
        Invitation invitation = new Invitation();
        invitation.setRoom(new Room("xXx"));
        invitation.setInvitedUser(SessionManager.getInstance().getConnectedUser());
        invitation.setInvitingUser(new User("Jean-Bertrand"));
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(invitation);
        return delegate.returningResponse(invitations).getPendingInvitation();
    }

    @Override
    public Observable<Invitation> inviteUserToRoom(@Path("id") String roomId, @Body User invitedUser) {
        Invitation invitation = new Invitation();
        invitation.setRoom(new Room(roomId));
        invitation.setInvitedUser(invitedUser);
        invitation.setInvitingUser(SessionManager.getInstance().getConnectedUser());
        return delegate.returningResponse(invitation).inviteUserToRoom(roomId, invitedUser);
    }

    @Override
    public Observable<List<Room>> acceptInvitationToRoom(@Body Invitation invitation) {
        return delegate.returningResponse(ROOMS).acceptInvitationToRoom(invitation);
    }

    @Override
    public Observable<Boolean> registerDevice(@Body String registrationId) {
        return delegate.returningResponse(Boolean.TRUE).registerDevice(registrationId);
    }

    @Override
    public Observable<List<Room>> getAllRooms() {
        return delegate.returningResponse(ROOMS).getAllRooms();
    }

    @Override
    public Observable<Room> getRoom(@Path("id") String roomId) {
        return null;
    }

    @Override
    public Observable<Room> createRoom(@Body Room room) {
        room.setId(UUID.randomUUID().toString());
        return delegate.returningResponse(room).createRoom(room);
    }

    @Override
    public Observable<Room> updateRoom(@Path("id") String roomId, @Body Room room) {
        return null;
    }

    @Override
    public Observable<List<Room>> leaveRoom(@Path("id") String roomId) {
        return delegate.returningResponse(ROOMS).leaveRoom(roomId);
    }

    @Override
    public Observable<List<Gift>> getGifts(@Path("roomId") String roomId) {
        List<Gift> gifts = new ArrayList<>();
        for (Room element : ROOMS) {
            if (element.getId().equals(roomId)) {
                gifts = element.getGifts();
            }
        }
        return delegate.returningResponse(gifts).getGifts(roomId);
    }

    @Override
    public Observable<Gift> getGift(@Path("roomId") String roomId, @Path("giftId") String giftId) {
        for (Room room : ROOMS) {
            for (Gift gift : room.getGifts()) {
                if (gift.getId().equals(giftId)) {
                    return delegate.returningResponse(gift).getGift(roomId, giftId);
                }
            }
        }
        return delegate.returningResponse(new NoSuchElementException("This gift is not in DB.")).getGift(roomId, giftId);
    }

    @Override
    public Observable<Gift> addGift(@Path("roomId") String roomId, @Part MultipartBody.Part file, @Part("gift") RequestBody gift) {
        // TODO: 28/03/2017 Extract gift from RequestBody
        if (MediaType.parse("application/json").equals(gift.contentType())) {
            Gift addedGift = new Gson().fromJson(gift.toString(), Gift.class);
            addedGift.setId(UUID.randomUUID().toString());
            return delegate.returningResponse(addedGift).addGift(roomId, file, gift);
        }
        return delegate.returningResponse(new JsonSyntaxException("Could not add the gift")).addGift(roomId, file, gift);
    }

    @Override
    public Observable<Gift> addGift(@Path("roomId") String roomId, @Body Gift gift) {
        return delegate.returningResponse(gift).addGift(roomId, gift);
    }

    @Override
    public Observable<Gift> updateGift(@Path("roomId") String roomId, @Path("id") String giftId, @Body Gift gift) {
        return delegate.returningResponse(gift).updateGift(roomId, giftId, gift);
    }

    @Override
    public Observable<List<Gift>> deleteGift(@Path("roomId") String roomId, @Path("giftId") String giftId) {
        for (Room room : ROOMS) {
            for (Gift gift : room.getGifts()) {
                if (gift.getId().equals(giftId)) {
                    return delegate.returningResponse(room.getGifts()).deleteGift(roomId, giftId);
                }
            }
        }
        return delegate.returningResponse(new NoSuchElementException("This gift is not in DB.")).deleteGift(roomId, giftId);
    }
}
