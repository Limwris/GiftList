package com.nichesoftware.giftlist.dataproviders;

import android.content.Context;
import android.content.SharedPreferences;

import com.nichesoftware.giftlist.model.User;

/**
 * Created by n_che on 06/06/2016.
 */
public final class PersistenceBroker {
    private static final String SHARED_PREFERENCES_GIFLIST_FILE = "SHARED_PREFERENCES_GIFLIST_FILE";
    private static final String USER_KEY = "USER_KEY";

    /**
     * Constructeur privé
     */
    private PersistenceBroker() {
        // Nothing
    }

    public static void setCurrentUser(final Context context, final String username) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_KEY, username);
        editor.apply();
    }

    public static String getCurrentUser(final Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        return sharedPref.getString(USER_KEY, null);
    }

    /**
     * Suppression des données en cache
     * @param context
     */
    public static void clearData(final Context context) {
        UserManager manager = new UserManager(context);
        // Ouverture de la table en lecture/écriture
        manager.open();
        // On récupère le username courant
        String username = getCurrentUser(context);
        User user = manager.retreiveUser(username);
        user.setRooms(null);
        manager.updateUser(user);
        // Fermeture du gestionnaire
        manager.close();
    }

    /**
     * Récupération du token de l'utilisateur en mémoire
     * @param context
     * @return
     */
    public static String retreiveUserToken(final Context context) {
        UserManager manager = new UserManager(context);
        // Ouverture de la table en lecture/écriture
        manager.open();
        // On récupère le username courant
        String username = getCurrentUser(context);
        User user = manager.retreiveUser(username);
        // Fermeture du gestionnaire
        manager.close();
        return user.getToken();
    }

    /**
     * Récupération du token de l'utilisateur en mémoire
     * @param context
     * @return
     */
    public static User retreiveUser(final Context context) {
        UserManager manager = new UserManager(context);
        // Ouverture de la table en lecture/écriture
        manager.open();
        // On récupère le username courant
        String username = getCurrentUser(context);
        User user = manager.retreiveUser(username);
        // Fermeture du gestionnaire
        manager.close();
        return user;
    }

    /**
     * Sauvegarde du token de l'utilisateur
     * @param context
     * @param user
     */
    public static void saveUser(final Context context, User user) {
        UserManager manager = new UserManager(context);
        // Ouverture de la table en lecture/écriture
        manager.open();
        manager.updateUser(user);
        // Fermeture du gestionnaire
        manager.close();
    }



    /**
     * Supprime les salles de l'utilisateur
     * @param context
     * @param user
     */
    public static void deleteRoomsFromUser(final Context context, User user) {
        user.setRooms(null);
        saveUser(context, user);
    }
}
