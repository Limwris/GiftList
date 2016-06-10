package com.nichesoftware.giftlist.dataproviders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.service.ServiceAPI;
import com.nichesoftware.giftlist.utils.StringUtils;

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

    public void logIn(@NonNull final String username,
                      @NonNull final String password,
                      @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("logIn [username = %s, password = %s]", username, password));
        }

        serviceApi.authenticate(username, password, new ServiceAPI.ServiceCallback<String>() {
            @Override
            public void onLoaded(String value) {
                PersistenceBroker.saveUserToken(context, value);
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
        final String token = PersistenceBroker.retreiveUserToken(context);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getRooms [token = %s]", token));
        }
        List<Room> rooms = PersistenceBroker.retreiveRooms(context);

        // Cas d'un utilisateur local
        if(StringUtils.isEmpty(token)) {
            callback.onRoomsLoaded(rooms);
        } else { // Cas d'un utilisateur connecté
            // Load from API only if needed.
            if (rooms == null) {
                serviceApi.getAllRooms(token, new ServiceAPI.ServiceCallback<List<Room>>() {
                    @Override
                    public void onLoaded(List<Room> rooms) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "getRooms - onLoaded");
                        }
                        PersistenceBroker.saveRooms(context, rooms);
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
        PersistenceBroker.clearData(context);
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
        final String token = PersistenceBroker.retreiveUserToken(context);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getGifts [token = %s]", token));
        }
        final List<Room> rooms = PersistenceBroker.retreiveRooms(context);
        Room room = getRoomFromId(rooms, roomId);

        // Cas d'un utilisateur local
        if(StringUtils.isEmpty(token)) {
            if (room != null) {
                callback.onGiftsLoaded(room.getGiftList());
            } else {
                // Todo: gestion d'erreur
                callback.onGiftsLoaded(null);
            }
        } else { // Cas d'un utilisateur connecté
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
                            Room room = getRoomFromId(rooms, roomId);

                            if (room != null) {
                                room.setGiftList(gifts);
                                PersistenceBroker.saveRooms(context, rooms);
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

    private Room getRoomFromId(final List<Room> rooms, final int roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                return room;
            }
        }
        return null;
    }

    public void createRoom(@NonNull final String roomName,
                           @NonNull final String occasion,
                           @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("createRoom [roomName = %s, occasion = %s]", roomName, occasion));
        }
        final String token = PersistenceBroker.retreiveUserToken(context);

        serviceApi.createRoom(token, roomName, occasion, new ServiceAPI.ServiceCallback<Boolean>() {
            @Override
            public void onLoaded(Boolean value) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "createRoom - onSuccess");
                }
                PersistenceBroker.clearData(context);
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });

    }

    public void addGift(int roomId, @NonNull final String name,
                        double price, double amount,
                        @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("addGift [name = %s, price = %f]", name, price));
        }
        final String token = PersistenceBroker.retreiveUserToken(context);

        serviceApi.addGift(token, roomId, name, price, amount,
                new ServiceAPI.ServiceCallback<Boolean>() {
                    @Override
                    public void onLoaded(Boolean value) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "addGift - onSuccess");
                        }
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

    public void updateGift(int roomId, int giftId,
                        double amount, @NonNull final Callback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("updateGift [giftId = %s, amount = %f]", giftId, amount));
        }
        final String token = PersistenceBroker.retreiveUserToken(context);

        serviceApi.updateGift(token, roomId, giftId, amount,
                new ServiceAPI.ServiceCallback<Boolean>() {
                    @Override
                    public void onLoaded(Boolean value) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "updateGift - onSuccess");
                        }
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
