package com.nichesoftware.giftlist.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 17/06/2016.
 */
public class UserManager {
    public static final String TABLE_NAME = "user";
    public static final String KEY_USENAME = "username";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_LIST_ROOMS = "rooms";
    public static final String CLAUSE_ID_WHERE = KEY_USENAME + " = ?";
    private static final String RETREIVE_ALL_RECORDS = "SELECT * FROM " + TABLE_NAME;
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + KEY_USENAME + " TEXT primary key, "
            + KEY_TOKEN + " TEXT, "
            + KEY_LIST_ROOMS + " TEXT);";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public UserManager(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void open() {
        // On ouvre la table en lecture/écriture
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        // On ferme l'accès à la BDD
        database.close();
    }

    /**********************************************************************************************/
    /********************                  CRUD OPERATIONS                     ********************/
    /**********************************************************************************************/

    /**
     * Create operation
     * @param user
     * @return
     */
    public long createUser(User user) {
        // Ajout d'un enregistrement dans la table
        ContentValues values = new ContentValues();
        values.put(KEY_USENAME, user.getUsername());
        values.put(KEY_TOKEN, user.getToken());
        values.put(KEY_LIST_ROOMS, toJson(user.getRooms()));

        return database.insert(TABLE_NAME, null, values);
    }

    /**
     * Retreive operation
     * @param username
     * @return
     */
    public User retreiveUser(String username) {
        // Retourne l'utilisateur dont le pseudonyme est passé en paramètre
        User user = new User();
        user.setUsername(username);

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE " + KEY_USENAME + " = '" + username + "'", null);
        if (c.moveToFirst()) {
            user.setRooms(fromJson(c.getString(c.getColumnIndex(KEY_LIST_ROOMS))));
            user.setToken(c.getString(c.getColumnIndex(KEY_TOKEN)));
            c.close();
        }

        return user;
    }


    /**
     * Update operation
     * @param user
     * @return
     */
    public int updateUser(User user) {
        // Modification d'un enregistrement
        ContentValues values = new ContentValues();
        values.put(KEY_USENAME, user.getUsername());
        values.put(KEY_TOKEN, user.getToken());
        values.put(KEY_LIST_ROOMS, toJson(user.getRooms()));

        String[] whereArgs = {String.valueOf(user.getUsername())};

        // Valeur de retour : (int) nombre de lignes affectées par la requête
        return database.update(TABLE_NAME, values, CLAUSE_ID_WHERE, whereArgs);
    }

    /**
     * Delete operation
     * @param user
     * @return
     */
    public int deleteUser(User user) {
        // Suppression d'un enregistrement
        String[] whereArgs = {String.valueOf(user.getUsername())};
        return database.delete(TABLE_NAME, CLAUSE_ID_WHERE, whereArgs);
    }

    /**
     * Delete all records
     * @return
     */
    public int clearUsers() {
        return database.delete(TABLE_NAME, null, null);
    }

    private String toJson(List<Room> rooms) {
        Gson gson = new Gson();
        return gson.toJson(rooms);
    }

    private List<Room> fromJson(final String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Room>>() {}.getType();
        return gson.fromJson(json, type);
    }

} // Class GuessManager
