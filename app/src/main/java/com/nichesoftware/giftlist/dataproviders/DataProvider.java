package com.nichesoftware.giftlist.dataproviders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.ServiceAPI;

import java.util.ArrayList;
import java.util.List;

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
        PersistenceBroker.clearData(context);

        serviceApi.authenticate(username, password, new ServiceAPI.ServiceCallback<String>() {
            @Override
            public void onLoaded(String value) {
                PersistenceBroker.setCurrentUser(context, username);

                User user = PersistenceBroker.retreiveUser(context);
                user.setToken(value);
                PersistenceBroker.saveUser(context, user);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("logIn - onSuccess | token = %s", value));
                }
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    /**
     * Récupère l'ensemble des salles
     * @param callback   - Callback
     */
    public void getRooms(@NonNull final LoadRoomsCallback callback) {
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
            if (rooms == null) {
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
    public void refreshData() {
        String username = PersistenceBroker.getCurrentUser(context);
        if (!username.equals(User.DISCONNECTED_USER)) { // Si l'utilisateur n'est pas local
            PersistenceBroker.clearData(context);
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
                idRoom = rooms.get(-1).getId() + 1;
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

    public void register(@NonNull final String username,
                         @NonNull final String password,
                         @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("register [username = %s, password = %s]", username, password));
        }
        // A l'enregistrement, on nettoie les données sur les salles
        PersistenceBroker.clearData(context);

        serviceApi.register(username, password, new ServiceAPI.ServiceCallback<String>() {
            @Override
            public void onLoaded(String value) {
                // Set current user
                PersistenceBroker.setCurrentUser(context, username);
                // Sauvegarde du nouvel utilisateur
                User user = new User();
                user.setUsername(username);
                user.setToken(value);
                PersistenceBroker.saveUser(context, user);

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("register - onSuccess | token = %s", value));
                }
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
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
