package com.nichesoftware.giftlist.dataproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.nichesoftware.giftlist.BaseApplication;
import com.nichesoftware.giftlist.dataproviders.events.AuthenticationFailedEvent;
import com.nichesoftware.giftlist.dataproviders.events.AuthenticationSucceededEvent;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.ServiceAPI;
import com.nichesoftware.giftlist.utils.BusProvider;
import com.nichesoftware.giftlist.utils.FileUtils;
import com.nichesoftware.giftlist.utils.StringUtils;
import com.nichesoftware.giftlist.views.start.LaunchScreenActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Data provider
 */
public class DataProvider {
    private static final String TAG = DataProvider.class.getSimpleName();

    /**
     * Service
     */
    private ServiceAPI serviceApi;

    /**
     * Constructeur
     * @param serviceApi
     */
    public DataProvider(final ServiceAPI serviceApi) {
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

    public String getCurrentUser() {
        Log.d(TAG, "getCurrentUser");
        final Context context = BaseApplication.getAppContext();
        return PersistenceBroker.getCurrentUser(context);
    }

    public void logIn(@NonNull final String username,
                      @NonNull final String password) {
        Log.d(TAG, String.format("logIn [username = %s, password = %s]", username, password));

        final Context context = BaseApplication.getAppContext();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String phoneNumber = telephonyManager.getLine1Number();
        Log.d(TAG, String.format("logIn [phoneNumber = %s]", phoneNumber));

        serviceApi.authenticate(username, password,
                phoneNumber, new ServiceAPI.ServiceCallback<String>() {
                    @Override
                    public void onLoaded(String value) {
                        Log.d(TAG, String.format("logIn - onSuccess | token = %s", value));
                        PersistenceBroker.setCurrentUser(context, username);

                        User user = PersistenceBroker.retreiveUser(context);
                        user.setToken(value);
                        user.setPhoneNumber(phoneNumber);
                        PersistenceBroker.saveUser(context, user);
                        Log.d(TAG, String.format("logIn - onSuccess | user = %s", user.toString()));

                        final String gcmToken = PersistenceBroker.getGcmToken(context);
                        Log.d(TAG, String.format("logIn - onSuccess | GCM token = %s", gcmToken));
                        // Si le token n'a pas été envoyé, alors le renvoyer au serveur
                        if (!StringUtils.isEmpty(gcmToken)
                                && !PersistenceBroker.isTokenSent(context)) {
                            Log.d(TAG, "logIn - onSuccess | GCM token not sent");
                            sendGcmTokenToServer(gcmToken);
                        }

                        BusProvider.post(new AuthenticationSucceededEvent());
                    }

                    @Override
                    public void onError() {
                        BusProvider.post(new AuthenticationFailedEvent());
                    }
                });
    }

    public void register(@NonNull final String username,
                         @NonNull final String password,
                         @NonNull final Callback callback) {
        Log.d(TAG, String.format("register [username = %s, password = %s]", username, password));
        final Context context = BaseApplication.getAppContext();

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String phoneNumber = telephonyManager.getLine1Number();
        Log.d(TAG, String.format("register [phoneNumber = %s]", phoneNumber));

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

                Log.d(TAG, String.format("register - onSuccess | token = %s", value));

                // A l'enrôllement, on enregistre aussi le token GCM
                final String gcmToken = PersistenceBroker.getGcmToken(context);
                Log.d(TAG, String.format("register - onSuccess | GCM token = %s", gcmToken));
                // Si le token n'a pas été envoyé, alors le renvoyer au serveur
                if (!StringUtils.isEmpty(gcmToken) && !PersistenceBroker.isTokenSent(context)) {
                    Log.d(TAG, "register - onSuccess | GCM token not sent");
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
        final Context context = BaseApplication.getAppContext();
        PersistenceBroker.clearRoomsData(context);
        PersistenceBroker.setCurrentUser(context, User.DISCONNECTED_USER);
        Intent intent = new Intent(context, LaunchScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void retreiveAvailableContacts(final int roomId,
                                          @NonNull final CallbackValue<List<User>> callback) {
        Log.d(TAG, "retreiveAvailableContacts");
        final Context context = BaseApplication.getAppContext();

        final String username = PersistenceBroker.getCurrentUser(context);
        Log.d(TAG, String.format("retreiveAvailableContacts [username = %s]", username));
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
        Log.d(TAG, "fetchContacts");
        final Context context = BaseApplication.getAppContext();

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
        final Context context = BaseApplication.getAppContext();
        final String username = PersistenceBroker.getCurrentUser(context);
        Log.d(TAG, String.format("getRooms [username = %s]", username));

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
                        Log.d(TAG, "getRooms - onLoaded");

                        // Todo: Revoir peut-être la façon de gérer ces paramètres
                        // Rg pour compléter le modèle
                        if (rooms != null) {
                            for (Room room : rooms) {
                                for (Gift gift : room.getGiftList()) {
                                    if (gift.getAmountByUser().containsKey(username)) {
                                        gift.setAmount(gift.getAmountByUser().get(username));
                                    }
                                }
                            }

                            user.setRooms(rooms);
                            PersistenceBroker.saveUser(context, user);
                            callback.onRoomsLoaded(rooms);
                        } else {
                            // Todo: Revoir la gestion des erreurs
                            callback.onRoomsLoaded(null);
                        }
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "getRooms - onError");
                        // Todo: Revoir la gestion des erreurs
                        callback.onRoomsLoaded(null);
                    }
                });
            } else {
                callback.onRoomsLoaded(rooms);
            }
        }
    }

    public void createRoom(@NonNull final String roomName,
                           @NonNull final String occasion,
                           @NonNull final Callback callback) {
        Log.d(TAG, String.format("createRoom [roomName = %s, occasion = %s]", roomName, occasion));
        final Context context = BaseApplication.getAppContext();
        final String username = PersistenceBroker.getCurrentUser(context);

        if (username.equals(User.DISCONNECTED_USER)) { // Si l'utilisateur est local
            // Increment id room if needed
            int idRoom = 0;
            User user = PersistenceBroker.retreiveUser(context);
            List<Room> rooms = user.getRooms();
            if (rooms == null || rooms.isEmpty()) {
                rooms = new ArrayList<>();
            } else {
                idRoom = rooms.get(rooms.size() - 1).getId() + 1;
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
                    Log.d(TAG, "createRoom - onSuccess");
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

    public void leaveRoom(final int roomId, @NonNull final Callback callback) {
        Log.d(TAG, String.format("leaveRoom [roomId = %d]", roomId));
        final Context context = BaseApplication.getAppContext();
        final String username = PersistenceBroker.getCurrentUser(context);

        if (username.equals(User.DISCONNECTED_USER)) { // Si l'utilisateur est local
            User user = PersistenceBroker.retreiveUser(context);
            List<Room> rooms = user.getRooms();

            for(Iterator<Room> iterator = rooms.iterator(); iterator.hasNext(); ) {
                Room room = iterator.next();
                if (room.getId() == roomId) {
                    // Suppression des images associées aux cadeaux de cette salle
                    for (Gift gift : room.getGiftList()) {
                        final String filePath = FileUtils.getPathOfFileName(context,
                                String.format(Locale.getDefault(), "%d%s", gift.getId(),
                                        FileUtils.JPEG_EXTENSION));
                        try {
                            Log.d(TAG, String.format(Locale.getDefault(),
                                    "leaveRoom - delete gift pic [file: %s]", filePath));
                            FileUtils.remove(filePath);
                        } catch (IOException ignored) {
                            Log.e(TAG, "leaveRoom - delete failed...", ignored);
                        }
                    }
                    iterator.remove();
                }
            }

            user.setRooms(rooms);
            PersistenceBroker.saveUser(context, user);
            callback.onSuccess();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);

            serviceApi.leaveRoom(token, roomId, new ServiceAPI.ServiceCallback<List<Room>>() {
                @Override
                public void onLoaded(List<Room> value) {
                    Log.d(TAG, "leaveRoom - onSuccess");
                    User user = PersistenceBroker.retreiveUser(context);
                    user.setRooms(value);
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
                        double price, double amount, final String description,
                        final String filePath, @NonNull final Callback callback) {
        Log.d(TAG, String.format("addGift [name = %s, price = %f]", name, price));
        final Context context = BaseApplication.getAppContext();

        final String username = PersistenceBroker.getCurrentUser(context);
        final User user = PersistenceBroker.retreiveUser(context);

        // Cas déconnecté
        if (username.equals(User.DISCONNECTED_USER)) {
            List<Room> rooms = user.getRooms();
            Room room = getRoomById(rooms, roomId);
            if (room != null) {
                Gift gift = new Gift();
                // Utilisation du timestamp pour créer un ID unique
                gift.setId((int) (new Date().getTime()/1000));
                gift.setName(name);
                gift.setAmount(amount);
                gift.setPrice(price);
                gift.getAmountByUser().put(username, amount);
                room.addGift(gift);

                if (!StringUtils.isEmpty(filePath)) {
                    final String imagePath = FileUtils.getPathOfFileName(context,
                            String.format(Locale.getDefault(),
                                    "%d%s", gift.getId(), FileUtils.JPEG_EXTENSION));
                    Log.d(TAG, String.format("addGift - disconnected [imagePath = %s]",
                            imagePath));
                    FileUtils.savePicToLocalFolder(filePath, imagePath);
                }
            }
            user.setRooms(rooms);
            PersistenceBroker.saveUser(context, user);
            callback.onSuccess();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.addGift(token, roomId, name, price, amount, description, filePath,
                    new ServiceAPI.ServiceCallback<Gift>() {
                        @Override
                        public void onLoaded(Gift value) {
                            Log.d(TAG, "addGift - onSuccess");
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
                            Log.d(TAG, "addGift - onError");
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
        final Context context = BaseApplication.getAppContext();
        final String username = PersistenceBroker.getCurrentUser(context);
        Log.d(TAG, String.format("getGifts [username = %s]", username));

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
                            Log.d(TAG, "getGifts - onLoaded");
                            // Todo: Revoir peut-être la façon de gérer ce paramètre
                            // Rg pour compléter le modèle
                            if (gifts != null) {
                                for (Gift gift : gifts) {
                                    if (gift.getAmountByUser().containsKey(username)) {
                                        gift.setAmount(gift.getAmountByUser().get(username));
                                    }
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
                            } else {
                                // Todo: gestion d'erreur
                                callback.onGiftsLoaded(null);
                            }
                        }

                        @Override
                        public void onError() {
                            Log.d(TAG, "getGifts - onError");
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

    public void updateGift(final int roomId, int giftId, double amount,
                           final String description, final String filePath,
                           @NonNull final Callback callback) {
        Log.d(TAG, String.format("updateGift [giftId = %s, amount = %f]", giftId, amount));

        final Context context = BaseApplication.getAppContext();
        final String username = PersistenceBroker.getCurrentUser(context);
        final User user = PersistenceBroker.retreiveUser(context);

        // Cas déconnecté
        if (username.equals(User.DISCONNECTED_USER)) {
            List<Room> rooms = user.getRooms();
            Room room = getRoomById(rooms, roomId);
            if (room != null) {
                Gift gift = room.getGiftById(giftId);
                gift.getAmountByUser().put(username, amount);
                gift.setAmount(amount);
                room.updateGift(gift);

                if (!StringUtils.isEmpty(filePath)) {
                    final String imagePath = FileUtils.getPathOfFileName(context,
                            String.format(Locale.getDefault(),
                                    "%d%s", gift.getId(), FileUtils.JPEG_EXTENSION));
                    Log.d(TAG, String.format("updateGift - disconnected [imagePath = %s]",
                            imagePath));
                    FileUtils.savePicToLocalFolder(filePath, imagePath);
                }
            }
            user.setRooms(rooms);
            PersistenceBroker.saveUser(context, user);
            callback.onSuccess();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.updateGift(token, roomId, giftId, amount, description, filePath,
                    new ServiceAPI.ServiceCallback<Gift>() {
                        @Override
                        public void onLoaded(Gift value) {
                            Log.d(TAG, "updateGift - onSuccess");
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
                            Log.d(TAG, "updateGift - onError");
                            callback.onError();
                        }
                    });
        }
    }

    public void inviteUserToRoom(final int roomId, @NonNull final String username,
                                 @NonNull final Callback callback) {
        Log.d(TAG, String.format("inviteUserToRoom [roomId = %s, username = %s]", roomId, username));

        final Context context = BaseApplication.getAppContext();
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
        Log.d(TAG, String.format("acceptInvitationToRoom [roomId = %d]", roomId));

        final Context context = BaseApplication.getAppContext();
        final String user = PersistenceBroker.getCurrentUser(context);
        // Cas déconnecté
        if (user.equals(User.DISCONNECTED_USER)) {
            callback.onError();
        } else {
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.acceptInvitationToRoom(token, roomId,
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
        Log.d(TAG, "sendGcmTokenToServer");

        final Context context = BaseApplication.getAppContext();
        final String username = PersistenceBroker.getCurrentUser(context);
        // Cas connecté
        if (!username.equals(User.DISCONNECTED_USER)) {
            // Sending the registration id to our server
            final String token = PersistenceBroker.retreiveUserToken(context);
            serviceApi.sendRegistrationToServer(token, gcmToken, new ServiceAPI.OnRegistrationCompleted() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Token registered successfully in server.");
                    PersistenceBroker.setTokenSent(context, true);
                }

                @Override
                public void onError() {
                    Log.d(TAG, "Failed to register token in server.");
                    PersistenceBroker.setTokenSent(context, false);
                }
            });
        }
    }

    public void registerGcm(final String gcmToken) {
        Log.d(TAG, String.format(Locale.getDefault(), "GCM Registration Token: %s", gcmToken));

        final Context context = BaseApplication.getAppContext();
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
                    Log.d(TAG, String.format("registerGcm - token for connected user %s", username));
                    sendGcmTokenToServer(gcmToken);
                }

            }
        }).start();
    }

    public String getGiftImageUrl(int giftId) {
        Log.d(TAG, String.format(Locale.getDefault(), "getGiftImageUrl [giftId: %d]", giftId));

        final Context context = BaseApplication.getAppContext();
        final String user = PersistenceBroker.getCurrentUser(context);
        // Cas déconnecté
        if (user.equals(User.DISCONNECTED_USER)) {
            final String filePath = FileUtils.getPathOfFileName(context,
                    String.format(Locale.getDefault(), "%d%s", giftId, FileUtils.JPEG_EXTENSION));
            Log.d(TAG, String.format(Locale.getDefault(),
                    "getGiftImageUrl - disconnected [path: %s]", filePath));
            return FileUtils.getContentUriFileName(context,
                    String.format(Locale.getDefault(), "%d%s", giftId, FileUtils.JPEG_EXTENSION));
        } else {
            return serviceApi.getGiftImageUrl(giftId);
        }
    }

    public Uri getContactImageUrl(final String phoneNumber) {
        Log.d(TAG, String.format(Locale.getDefault(),
                "getContactImageUrl [phone number: %s]", phoneNumber));
        long contactId = getContactIdFromPhoneNumber(phoneNumber);
        if (contactId > 0) {
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            Log.d(TAG, String.format(Locale.getDefault(),
                    "getContactImageUrl [uri: %s]", contactUri.toString()));
            return Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        }

        return null;
    }

    private long getContactIdFromPhoneNumber(final String phoneNumber) {
        Log.d(TAG, "getContactIdFromPhoneNumber");

        final Context context = BaseApplication.getAppContext();
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = contentResolver.query(uri,
                new String[] { ContactsContract.PhoneLookup._ID },
                null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            Log.d(TAG, "getContactIdFromPhoneNumber - contact found");
            long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
            Log.d(TAG, String.format(Locale.getDefault(),
                    "getContactIdFromPhoneNumber - contact found with id %d", contactId));
            return contactId;
        }

        return -1;
    }


    /**
     * Indique si c'est l'utilisateur hors connexion
     * @return
     */
    public boolean isDisconnectedUser() {
        final Context context = BaseApplication.getAppContext();
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
