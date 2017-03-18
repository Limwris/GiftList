package com.nichesoftware.giftlist.dataproviders;

import android.content.Context;
import android.content.SharedPreferences;

import com.nichesoftware.giftlist.model.User;

import java.util.List;

/**
 * User share preference persistence manager
 */
public final class PersistenceBroker {
    private static final String SHARED_PREFERENCES_GIFLIST_FILE = "SHARED_PREFERENCES_GIFLIST_FILE";
    private static final String USER_KEY = "USER_KEY";
    public static final String GCM_TOKEN_KEY = "GCM_TOKEN_KEY";

    /**
     * Constructeur privé
     */
    private PersistenceBroker() {
        // Nothing
    }

    /**
     * Setter sur le current User
     * @param context
     * @param username
     */
    public static void setCurrentUser(final Context context, final String username) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_KEY, username);
        editor.apply();
    }

    /**
     * Retourne l'utilisateur courant, ou DISCONECTED_USER sinon
     * @param context
     * @return
     */
    public static String getCurrentUser(final Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        return sharedPref.getString(USER_KEY, User.DISCONNECTED_USER);
    }

    /**
     * Suppression des données en cache
     * @param context
     */
    public static void clearRoomsData(final Context context) {
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
        int s = manager.updateUser(user);
        if (s == 0) { // Alors l'utilisateur n'a pas encore été sauvegardé dans la base
            manager.createUser(user);
        }
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

    /**
     * Supprime le GCM token enregistré
     * @param context
     */
    public static void invalidateGcmToken(Context context) {
        setGcmToken(context, null);

        // On indique que le token doit être envoyé de nouveau au serveur
        UserManager manager = new UserManager(context);
        // Ouverture de la table en lecture/écriture
        manager.open();

        List<User> users = manager.retreiveAllUsers();
        for(User user : users) {
            user.setIsTokenSent(false);
            saveUser(context, user);
        }
        // Fermeture du gestionnaire
        manager.close();
    }

    /**
     * Setter sur le token GCM
     * @param context
     * @param gcmToken
     */
    public static void setGcmToken(Context context, final String gcmToken) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GCM_TOKEN_KEY, gcmToken);
        editor.apply();
    }

    /**
     * Retourne le GCM token enregistré
     * @param context
     * @return
     */
    public static String getGcmToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_GIFLIST_FILE,
                Context.MODE_PRIVATE);
        return sharedPref.getString(GCM_TOKEN_KEY, null);
    }

    /**
     * Retourne le flag indiquant si le token a été envoyé au serveur
     * @param context
     * @return
     */
    public static boolean isTokenSent(Context context) {
        UserManager manager = new UserManager(context);
        // Ouverture de la table en lecture/écriture
        manager.open();
        // On récupère le username courant
        String username = getCurrentUser(context);
        User user = manager.retreiveUser(username);
        // Fermeture du gestionnaire
        manager.close();
        return user.isTokenSent();
    }

    /**
     * Setter sur le flag indiquant si le token a été envoyé au server
     * @param context
     * @param isSent
     */
    public static void setTokenSent(Context context, final boolean isSent) {
        UserManager manager = new UserManager(context);
        // Ouverture de la table en lecture/écriture
        manager.open();
        // On récupère le username courant
        String username = getCurrentUser(context);
        User user = manager.retreiveUser(username);
        user.setIsTokenSent(isSent);
        saveUser(context, user);
        // Fermeture du gestionnaire
        manager.close();
    }
}
