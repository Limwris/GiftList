package com.nichesoftware.giftlist.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.model.User;

/**
 * Created by n_che on 17/06/2016.
 */
public class DatabaseHelper  extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "giftlist.sqlite";
    private static final int DATABASE_VERSION = 8;
    private static DatabaseHelper INSTANCE;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) { INSTANCE = new DatabaseHelper(context); }
        return INSTANCE;
    }

    /**
     * Constructeur défini privé (pattern singleton)
     * @param context
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Création de la base de données
        // on exécute ici les requêtes de création des tables
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }
        sqLiteDatabase.execSQL(UserManager.CREATE_TABLE); // création tables

        // Create disconnected user
        ContentValues values = new ContentValues();
        values.put(UserManager.KEY_USENAME, User.DISCONNECTED_USER);
        values.put(UserManager.KEY_TOKEN, "");
        values.put(UserManager.KEY_LIST_ROOMS, "");
        sqLiteDatabase.insert(UserManager.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Mise à jour de la base de données
        // méthode appelée sur incrémentation de DATABASE_VERSION
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onUpgrade");
        }

        // On peut faire ce qu'on veut ici, comme supprimer la base puis la recréer:
        sqLiteDatabase.execSQL(UserManager.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

} // class DatabaseHelper