package com.nichesoftware.giftlist.dataproviders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.ServiceAPI;
import com.nichesoftware.giftlist.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by n_che on 25/04/2016.
 */
public class DataProvider {
    private static final String TAG = DataProvider.class.getSimpleName();

    /**
     * Service
     */
    private ServiceAPI serviceApi;
    /**
     * Contexte de l'application
     */
    private Context context;

    /**
     * Constructeur
     * @param serviceApi
     */
    public DataProvider(final Context context, final ServiceAPI serviceApi) {
        this.context = context;
        this.serviceApi = serviceApi;
    }

    public interface LoadRoomsCallback {
        void onRoomsLoaded(List<Room> rooms);
    }

    public interface LoadGiftsCallback {
        void onGiftsLoaded(List<Gift> gifts);
    }

    public interface Callback {
        void onSuccess();
        void onError();
    }

    public interface CallbackValue<T> {
        void onSuccess(T value);
        void onError();
    }

    public void logInDisconnected(@NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("logInDisconnected"));
        }
        PersistenceBroker.setCurrentUser(context, User.DISCONNECTED_USER);
        callback.onSuccess();
    }

    public void logIn(@NonNull final String username,
                      @NonNull final String password,
                      @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("logIn [username = %s, password = %s]", username, password));
        }
        // A l'authent, on clean les données sur les salles
        PersistenceBroker.clearRoomsData(context);

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String phoneNumber = telephonyManager.getLine1Number();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("logIn [phoneNumber = %s]", phoneNumber));
        }

        serviceApi.authenticate(username, password,
                phoneNumber, new ServiceAPI.ServiceCallback<String>() {
                    @Override
                    public void onLoaded(String value) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, String.format("logIn - onSuccess | token = %s", value));
                        }
                        PersistenceBroker.setCurrentUser(context, username);

                        User user = PersistenceBroker.retreiveUser(context);
                        user.setToken(value);
                        user.setPhoneNumber(phoneNumber);
                        PersistenceBroker.saveUser(context, user);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, String.format("logIn - onSuccess | user = %s", user.toString()));
                        }

                        final String gcmToken = PersistenceBroker.getGcmToken(context);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, String.format("logIn - onSuccess | GCM token = %s", gcmToken));
                        }
                        // Si le token n'a pas été envoyé, alors le renvoyer au serveur
                        if (!StringUtils.isEmpty(gcmToken)
                                && !PersistenceBroker.isTokenSent(context)) {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, String.format("logIn - onSuccess | GCM token not sent"));
                            }
                            sendGcmTokenToServer(gcmToken);
                        }

                        callback.onSuccess();
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
    }

    public void register(@NonNull final String username,
                         @NonNull final String password,
                         @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("register [username = %s, password = %s]", username, password));
        }
        // A l'enregistrement, on nettoie les données sur les salles
        PersistenceBroker.clearRoomsData(context);

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String phoneNumber = telephonyManager.getLine1Number();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("register [phoneNumber = %s]", phoneNumber));
        }

        serviceApi.register(username, password, phoneNumber, new ServiceAPI.ServiceCallback<String>() {
            @Override
            public void onLoaded(String value) {
                // Set current user
                PersistenceBroker.setCurrentUser(context, username);
                // Sauvegarde du nouvel utilisateur
                User user = new User();
                user.setUsername(username);
                user.setPhoneNumber(phoneNumber);
                user.setToken(value);
                PersistenceBroker.saveUser(context, user);

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("register - onSuccess | token = %s", value));
                }

                // A l'enrôllement, on enregistre aussi le token GCM
                final String gcmToken = PersistenceBroker.getGcmToken(context);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("register - onSuccess | GCM token = %s", gcmToken));
                }
                // Si le token n'a pas été envoyé, alors le renvoyer au serveur
                if (!StringUtils.isEmpty(gcmToken) && !PersistenceBroker.isTokenSent(context)) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, String.format("register - onSuccess | GCM token not sent"));
                    }
                    sendGcmTokenToServer(gcmToken);
                }

                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    public void doDisconnect() {
        PersistenceBroker.setCurrentUser(context, User.DISCONNECTED_USER);
    }

    public void retreiveAvailableContacts(final int roomId,
                                          @NonNull final CallbackValue<List<User>> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("retreiveAvailableContacts"));
        }

        final String username = PersistenceBroker.getCurrentUser(context);
        // Cas déconnecté
        if (username.equals(User.DISCONNECTED_USER)) {
            callback.onError();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);
            List<String> phoneNumbers = fetchContacts();

            serviceApi.retreiveAvailableUsers(token, roomId, phoneNumbers,
                    new ServiceAPI.ServiceCallback<List<User>>() {
                        @Override
                        public void onLoaded(List<User> value) {
                            callback.onSuccess(value);
                        }

                        @Override
                        public void onError() {
                            callback.onError();
                        }
                    });
        }

    }

    private List<String> fetchContacts() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("fetchContacts"));
        }

        List<String> phoneNumbers = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + " != 0",
                null,
                null);

        // Loop for every contact in the phone
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[] { contact_id },
                        null);

                if (phoneCursor!= null) {
                    while (phoneCursor.moveToNext()) {
                        int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            Locale locale = context.getResources().getConfiguration().locale;
                            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
                            Phonenumber.PhoneNumber pn = null;

                            try {
                                pn = pnu.parse(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                                        locale.getCountry());
                            } catch (NumberParseException ignored) { }

                            if (pn != null) {
                                String phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.E164);

                                if (!phoneNumbers.contains(phoneNumber)) {
                                    phoneNumbers.add(phoneNumber);
                                }
                            }
                        }
                    }

                    phoneCursor.close();
                }
            }

            cursor.close();
        }

        return phoneNumbers;
    }

    /**
     * Récupère l'ensemble des salles
     * @param callback   - Callback
     */
    public void getRooms(final boolean forceUpdate,
            @NonNull final LoadRoomsCallback callback) {
        final String username = PersistenceBroker.getCurrentUser(context);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getRooms [username = %s]", username));
        }

        final User user = PersistenceBroker.retreiveUser(context);
        List<Room> rooms = user.getRooms();

        if (username.equals(User.DISCONNECTED_USER)) { // Si l'utilisateur est local
            callback.onRoomsLoaded(rooms);
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);

            // Load from API only if needed.
            if (rooms == null || forceUpdate) {
                serviceApi.getAllRooms(token, new ServiceAPI.ServiceCallback<List<Room>>() {
                    @Override
                    public void onLoaded(List<Room> rooms) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "getRooms - onLoaded");
                        }
                        user.setRooms(rooms);
                        PersistenceBroker.saveUser(context, user);
                        callback.onRoomsLoaded(rooms);
                    }

                    @Override
                    public void onError() {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "getRooms - onError");
                        }
                        // Todo: Revoir la gestion des erreurs
                        callback.onRoomsLoaded(null);
                    }
                });
            } else {
                callback.onRoomsLoaded(rooms);
            }
        }
    }

    /**
     * Méthode appelée pour forcer le rafraîchissement des données lors du prochain appel
     */
//    public void refreshData() {
//        String username = PersistenceBroker.getCurrentUser(context);
//        if (!username.equals(User.DISCONNECTED_USER)) { // Si l'utilisateur n'est pas local
//            PersistenceBroker.clearRoomsData(context);
//        }
//    }

    public void createRoom(@NonNull final String roomName,
                           @NonNull final String occasion,
                           @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("createRoom [roomName = %s, occasion = %s]", roomName, occasion));
        }
        final String username = PersistenceBroker.getCurrentUser(context);

        if (username.equals(User.DISCONNECTED_USER)) { // Si l'utilisateur est local
            // Increment id room if needed
            int idRoom = 0;
            User user = PersistenceBroker.retreiveUser(context);
            List<Room> rooms = user.getRooms();
            if (rooms != null) {
                idRoom = rooms.get(rooms.size() - 1).getId() + 1;
            } else {
                rooms = new ArrayList<>();
            }
            Room room = new Room(idRoom, roomName, occasion);
            rooms.add(room);
            user.setRooms(rooms);
            PersistenceBroker.saveUser(context, user);
            callback.onSuccess();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);

            serviceApi.createRoom(token, roomName, occasion, new ServiceAPI.ServiceCallback<Room>() {
                @Override
                public void onLoaded(Room value) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "createRoom - onSuccess");
                    }
                    User user = PersistenceBroker.retreiveUser(context);
                    List<Room> rooms = user.getRooms();
                    rooms.add(value);
                    user.setRooms(rooms);
                    PersistenceBroker.saveUser(context, user);
                    callback.onSuccess();
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }

    }

    public void addGift(final int roomId, @NonNull final String name,
                        double price, double amount,
                        @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("addGift [name = %s, price = %f]", name, price));
        }

        final String username = PersistenceBroker.getCurrentUser(context);
        final User user = PersistenceBroker.retreiveUser(context);

        // Cas déconnecté
        if (username.equals(User.DISCONNECTED_USER)) {
            List<Room> rooms = user.getRooms();
            Room room = getRoomById(rooms, roomId);
            if (room != null) {
                Gift gift = new Gift();
                gift.setName(name);
                gift.setPrice(price);
                gift.getAmountByUser().put(username, amount);
                room.addGift(gift);
            }
            user.setRooms(rooms);
            PersistenceBroker.saveUser(context, user);
            callback.onSuccess();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.addGift(token, roomId, name, price, amount,
                    new ServiceAPI.ServiceCallback<Gift>() {
                        @Override
                        public void onLoaded(Gift value) {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "addGift - onSuccess");
                            }
                            List<Room> rooms = user.getRooms();
                            Room room = getRoomById(rooms, roomId);
                            if (room != null) {
                                room.addGift(value);
                            }
                            user.setRooms(rooms);
                            PersistenceBroker.saveUser(context, user);
                            callback.onSuccess();
                        }

                        @Override
                        public void onError() {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "addGift - onError");
                            }
                            callback.onError();
                        }
                    });
        }
    }

    /**
     * Récupère les cadeaux associés à une salle
     * @param forceUpdate   - Flag indiquant l'appel au webservice
     * @param roomId        - Identifiant de la salle
     * @param callback      - Callback
     */
    public void getGifts(final boolean forceUpdate,
                         @NonNull final int roomId,
                         @NonNull final LoadGiftsCallback callback) {
        final String username = PersistenceBroker.getCurrentUser(context);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getGifts [username = %s]", username));
        }

        final User user = PersistenceBroker.retreiveUser(context);
        List<Room> rooms = user.getRooms();
        Room room = getRoomById(rooms, roomId);

        if (username.equals(User.DISCONNECTED_USER)) { // Si l'utilisateur est local
            if (room != null) {
                callback.onGiftsLoaded(room.getGiftList());
            } else {
                // Todo: gestion d'erreur
                callback.onGiftsLoaded(null);
            }
        } else { // Cas d'un utilisateur connecté
            final String token = PersistenceBroker.retreiveUserToken(context);

            if (room == null) { // Ne devrait jamais arriver...
                // Todo: Revoir la gestion des erreurs
                callback.onGiftsLoaded(null);
            } else {
                if (forceUpdate) {
                    serviceApi.getGifts(token, roomId, new ServiceAPI.ServiceCallback<List<Gift>>() {
                        @Override
                        public void onLoaded(List<Gift> gifts) {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "getGifts - onLoaded");
                            }
                            List<Room> rooms = user.getRooms();
                            Room room = getRoomById(rooms, roomId);

                            if (room != null) {
                                room.setGiftList(gifts);
                                user.setRooms(rooms);
                                PersistenceBroker.saveUser(context, user);
                                callback.onGiftsLoaded(gifts);
                            } else {
                                // Todo: gestion d'erreur
                                callback.onGiftsLoaded(null);
                            }
                        }

                        @Override
                        public void onError() {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "getGifts - onError");
                            }
                            // Todo: Revoir la gestion des erreurs
                            callback.onGiftsLoaded(null);
                        }
                    });
                } else {
                    callback.onGiftsLoaded(room.getGiftList());
                }
            }
        }
    }

    public void updateGift(final int roomId, int giftId,
                        double amount, @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("updateGift [giftId = %s, amount = %f]", giftId, amount));
        }

        final String username = PersistenceBroker.getCurrentUser(context);
        final User user = PersistenceBroker.retreiveUser(context);

        // Cas déconnecté
        if (username.equals(User.DISCONNECTED_USER)) {
            List<Room> rooms = user.getRooms();
            Room room = getRoomById(rooms, roomId);
            if (room != null) {
                Gift gift = room.getGiftById(giftId);
                gift.getAmountByUser().put(username, amount);
                room.addGift(gift);
            }
            user.setRooms(rooms);
            PersistenceBroker.saveUser(context, user);
            callback.onSuccess();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.updateGift(token, roomId, giftId, amount,
                    new ServiceAPI.ServiceCallback<Gift>() {
                        @Override
                        public void onLoaded(Gift value) {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "updateGift - onSuccess");
                            }
                            List<Room> rooms = user.getRooms();
                            Room room = getRoomById(rooms, roomId);
                            if (room != null) {
                                room.updateGift(value);
                            }
                            user.setRooms(rooms);
                            PersistenceBroker.saveUser(context, user);
                            callback.onSuccess();
                        }

                        @Override
                        public void onError() {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "updateGift - onError");
                            }
                            callback.onError();
                        }
                    });
        }
    }

    public void inviteUserToRoom(final int roomId, @NonNull final String username,
                                 @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("inviteUserToRoom [roomId = %s, username = %s]", roomId, username));
        }

        final String user = PersistenceBroker.getCurrentUser(context);
        // Cas déconnecté
        if (user.equals(User.DISCONNECTED_USER)) {
            callback.onError();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.inviteUserToRoom(token, roomId, username,
                    new ServiceAPI.ServiceCallback<Boolean>() {
                        @Override
                        public void onLoaded(Boolean value) {
                            callback.onSuccess();
                        }

                        @Override
                        public void onError() {
                            callback.onError();
                        }
                    });
        }
    }

    public void acceptInvitationToRoom(final int roomId, @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("acceptInvitationToRoom [roomId = %d]", roomId));
        }

        final String user = PersistenceBroker.getCurrentUser(context);
        // Cas déconnecté
        if (user.equals(User.DISCONNECTED_USER)) {
            callback.onError();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.acceptionInvitationToRoom(token, roomId,
                    new ServiceAPI.ServiceCallback<Boolean>() {
                        @Override
                        public void onLoaded(Boolean value) {
                            callback.onSuccess();
                        }

                        @Override
                        public void onError() {
                            callback.onError();
                        }
                    });
        }
    }

    public void sendGcmTokenToServer(final String gcmToken) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "sendGcmTokenToServer");
        }

        final String username = PersistenceBroker.getCurrentUser(context);
        // Cas connecté
        if (!username.equals(User.DISCONNECTED_USER)) {
            // Sending the registration id to our server
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.sendRegistrationToServer(token, gcmToken, new ServiceAPI.OnRegistrationCompleted() {
                @Override
                public void onSuccess() {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Token registered token successfully in server.");
                    }
                    PersistenceBroker.setTokenSent(context, true);
                }

                @Override
                public void onError() {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Failed to registered token in server.");
                    }
                    PersistenceBroker.setTokenSent(context, false);
                }
            });
        }
    }

    public void registerGcm(final String gcmToken) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "GCM Registration Token: " + gcmToken);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Invalidate previously saved GCM token
                PersistenceBroker.invalidateGcmToken(context);

                // Save the token locally
                PersistenceBroker.setGcmToken(context, gcmToken);

                final String username = PersistenceBroker.getCurrentUser(context);
                // Cas connecté
                if (!StringUtils.isEmpty(username) && !username.equals(User.DISCONNECTED_USER)) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, String.format("registerGcm - token for connected user %s", username));
                    }
                    sendGcmTokenToServer(gcmToken);
                }

            }
        }).start();
    }

    /**
     * Indique si c'est l'utilisateur hors connexion
     * @return
     */
    public boolean isDisconnectedUser() {
        final String user = PersistenceBroker.getCurrentUser(context);
        return user.equals(User.DISCONNECTED_USER);
    }

    /**
     * Recherche une salle dans la liste des salles de l'utilisateur
     * @param rooms  - Liste des salles
     * @param roomId - identifiant de la salle recherchée
     * @return room  - salle correspondant à l'identifiant passé en paramètre, nul sinon
     */
    private Room getRoomById(List<Room> rooms, final int roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                return room;
            }
        }
        return null;
    }
}
