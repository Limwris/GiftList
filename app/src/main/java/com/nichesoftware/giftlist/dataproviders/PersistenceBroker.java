package com.nichesoftware.giftlist.dataproviders;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nichesoftware.giftlist.model.Room;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by n_che on 06/06/2016.
 */
public final class PersistenceBroker {
    private static final String SHARED_PREFERENCES_GIFLIST_FILE = "SHARED_PREFERENCES_GIFLIST_FILE";
    private static final String USER_TOKEN_KEY = "USER_TOKEN_KEY";
    private static final String ROOMS_KEY = "ROOMS_KEY";

    /**
     * Constructeur privé
     */
    private PersistenceBroker() {
        // Nothing
    }

    /**
     * Suppression des données en cache
     * @param context
     */
    public static void clearData(final Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(ROOMS_KEY);
        editor.apply();
    }

    /**
     * Récupération du token de l'utilisateur en mémoire
     * @param context
     * @return
     */
    public static String retreiveUserToken(final Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        return sharedPref.getString(USER_TOKEN_KEY, null);
    }

    /**
     * Sauvegarde du token de l'utilisateur
     * @param context
     * @param token
     */
    public static void saveUserToken(final Context context, final String token) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_TOKEN_KEY, token);
        editor.apply();
    }

    /**
     * Récupération des salles en mémoire
     * @param context
     * @return rooms   - Liste de salle de l'utilisateur local
     */
    public static List<Room> retreiveRooms(final Context context) {
        Gson gson = new GsonBuilder().create();
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        String json = sharedPref.getString(ROOMS_KEY, null);
        Type type = new TypeToken<List<Room>>() {}.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Sauvegarde des salles en mémoire
     * @param context
     * @param rooms   - Liste de salle de l'utilisateur local
     */
    public static void saveRooms(final Context context, final List<Room> rooms) {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(rooms);

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ROOMS_KEY, json);
        editor.apply();
    }
}
