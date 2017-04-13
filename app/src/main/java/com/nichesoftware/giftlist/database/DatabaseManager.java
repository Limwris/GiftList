package com.nichesoftware.giftlist.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nichesoftware.giftlist.BaseApplication;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * C
 */
public class DatabaseManager extends SQLiteOpenHelper {
    // Tag
    private static final String TAG = DatabaseManager.class.getSimpleName();

    private static DatabaseManager INSTANCE;

    public static synchronized DatabaseManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseManager();
        }
        return INSTANCE;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
//    private DatabaseManager(Context context) {
    private DatabaseManager() {
        super(BaseApplication.getAppContext(), DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    // region Database implementation
    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GIFTS_TABLE = "CREATE TABLE " + DatabaseContract.GiftEntry.TABLE_GIFTS +
                "(" +
                DatabaseContract.GiftEntry.KEY_GIFT_ID + " TEXT PRIMARY KEY," + // Define a primary key
                DatabaseContract.GiftEntry.KEY_GIFT_ROOM_ID_FK + " INTEGER REFERENCES " + DatabaseContract.RoomEntry.TABLE_ROOMS + "," + // Define a foreign key
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT + " REAL," +
                DatabaseContract.GiftEntry.KEY_GIFT_PRICE + " REAL," +
                DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER + " REAL," +
                DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION + " TEXT," +
                DatabaseContract.GiftEntry.KEY_GIFT_NAME + " TEXT," +
                DatabaseContract.GiftEntry.KEY_GIFT_URL + " TEXT," +
                DatabaseContract.GiftEntry.KEY_GIFT_IMAGE + " TEXT," +
                DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG + " INTEGER," +
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER + " TEXT" +
                ")";

        String CREATE_ROOMS_TABLE = "CREATE TABLE " + DatabaseContract.RoomEntry.TABLE_ROOMS +
                "(" +
                DatabaseContract.RoomEntry.KEY_ROOM_ID + " TEXT PRIMARY KEY," + // Define a primary key
                DatabaseContract.RoomEntry.KEY_ROOM_USER_ID_FK + " INTEGER REFERENCES " + DatabaseContract.UserEntry.TABLE_USERS + "," + // Define a foreign key
                DatabaseContract.RoomEntry.KEY_ROOM_NAME + " TEXT," +
                DatabaseContract.RoomEntry.KEY_ROOM_OCCASION + " TEXT" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + DatabaseContract.UserEntry.TABLE_USERS +
                "(" +
                DatabaseContract.UserEntry.KEY_USER_ID + " TEXT PRIMARY KEY," +
                DatabaseContract.UserEntry.KEY_USER_TOKEN + " TEXT," +
                DatabaseContract.UserEntry.KEY_USER_TOKEN_SENT + " TEXT" +
                ")";

        db.execSQL(CREATE_GIFTS_TABLE);
        db.execSQL(CREATE_ROOMS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL(DatabaseContract.DROP_QUERY + DatabaseContract.GiftEntry.TABLE_GIFTS);
            db.execSQL(DatabaseContract.DROP_QUERY + DatabaseContract.RoomEntry.TABLE_ROOMS);
            db.execSQL(DatabaseContract.DROP_QUERY + DatabaseContract.UserEntry.TABLE_USERS);
            onCreate(db);
        }
    }
    // endregion

    // region Room
    public void addOrUpdateRoom(Room room, String userId) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();

        Gson gson = new Gson();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.RoomEntry.KEY_ROOM_ID, room.getId());
            values.put(DatabaseContract.RoomEntry.KEY_ROOM_NAME, room.getName());
            values.put(DatabaseContract.RoomEntry.KEY_ROOM_OCCASION, room.getOccasion());
            values.put(DatabaseContract.RoomEntry.KEY_ROOM_USER_ID_FK, userId);

            // First try to update the room in case it already exists in the database
            int rows = db.update(DatabaseContract.RoomEntry.TABLE_ROOMS, values, DatabaseContract.RoomEntry.KEY_ROOM_ID + "= ?", new String[]{room.getId()});

            // Check if update succeeded
            if (rows < 1) {
                // user with this username did not already exist, so insert new user
                db.insertOrThrow(DatabaseContract.RoomEntry.TABLE_ROOMS, null, values);
            }

            if (room.getGifts() != null) {
                for (Gift gift : room.getGifts()) {
                    ContentValues giftValues = new ContentValues();
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_ID, gift.getId());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_ROOM_ID_FK, room.getId());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_NAME, gift.getName());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT, gift.getAmount());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_PRICE, gift.getPrice());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER, gift.getRemainder());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION, gift.getDescription());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_URL, gift.getUrl());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_IMAGE, gift.getImageUrl());
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG, gift.isImageFileLocal() ? 1 : 0);
                    giftValues.put(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER, gson.toJson(gift.getAmountByUser()));

                    // First try to update the gift in case it already exists in the database
                    Log.d(TAG, "addOrUpdateGift: Try to update the gift in case it already exists in the database");
                    int giftRows = db.update(DatabaseContract.GiftEntry.TABLE_GIFTS, giftValues, DatabaseContract.GiftEntry.KEY_GIFT_ID + "= ?", new String[]{ gift.getId() });

                    // Check if update succeeded
                    if (giftRows < 1) {
                        // gift with this id did not already exist, so insert new gift
                        Log.d(TAG, "addOrUpdateGift: Gift with this id did not already exist, so insert new gift");
                        db.insertOrThrow(DatabaseContract.GiftEntry.TABLE_GIFTS, null, giftValues);
                    }
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to add or update a room " + room + " for user " + userId, ignored);
        } finally {
            db.endTransaction();
        }
    }

    public @Nullable Room getRoom(String roomId) {
        Room room = null;

        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Double>>(){}.getType();

        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();

        String [] settingsProjection = {
                DatabaseContract.RoomEntry.KEY_ROOM_ID,
                DatabaseContract.RoomEntry.KEY_ROOM_NAME,
                DatabaseContract.RoomEntry.KEY_ROOM_OCCASION
        };

        String [] giftSettingsProjection = {
                DatabaseContract.GiftEntry.KEY_GIFT_ID,
                DatabaseContract.GiftEntry.KEY_GIFT_NAME,
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT,
                DatabaseContract.GiftEntry.KEY_GIFT_PRICE,
                DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER,
                DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION,
                DatabaseContract.GiftEntry.KEY_GIFT_URL,
                DatabaseContract.GiftEntry.KEY_GIFT_IMAGE,
                DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG,
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER
        };

        String whereClause = DatabaseContract.RoomEntry.KEY_ROOM_ID + "=?";
        String giftWhereClause = DatabaseContract.GiftEntry.KEY_GIFT_ROOM_ID_FK + "=?";
        String [] whereArgs = {roomId};

        Cursor cursor = db.query(
                DatabaseContract.RoomEntry.TABLE_ROOMS,     // The table to query
                settingsProjection,                         // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                                  // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );

        if (cursor.moveToFirst()) {

            List<Gift> gifts = new ArrayList<>();

            Cursor giftCursor = db.query(
                    DatabaseContract.GiftEntry.TABLE_GIFTS,     // The table to query
                    giftSettingsProjection,                     // The columns to return
                    giftWhereClause,                            // The columns for the WHERE clause
                    whereArgs,                                  // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );

            if (giftCursor.moveToFirst()) {
                do {
                    Gift gift = new Gift(giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_ID)),
                            giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_NAME)),
                            giftCursor.getDouble(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT)),
                            giftCursor.getDouble(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_PRICE)),
                            giftCursor.getDouble(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER)),
                            giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION)),
                            giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_URL)),
                            giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_IMAGE)),
                            giftCursor.getInt(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG)) > 0);
                    Map<String, Double> amoutPerUser =
                            gson.fromJson(giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER)),
                                    mapType);
                    gift.setAmountByUser(amoutPerUser);
                    gifts.add(gift);
                } while(giftCursor.moveToNext());
            }
            giftCursor.close();

            room = new Room(cursor.getString(cursor.getColumnIndex(DatabaseContract.RoomEntry.KEY_ROOM_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.RoomEntry.KEY_ROOM_NAME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.RoomEntry.KEY_ROOM_OCCASION)),
                    gifts);
            db.setTransactionSuccessful();
        }
        cursor.close();
        db.endTransaction();

        return room;
    }

    public void deleteRooms(final String userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            String [] settingsProjection = {
                    DatabaseContract.RoomEntry.KEY_ROOM_ID,
            };

            String whereClause = DatabaseContract.RoomEntry.KEY_ROOM_USER_ID_FK + "=?";
            String [] whereArgs = {userId};

            Cursor cursor = db.query(
                    DatabaseContract.RoomEntry.TABLE_ROOMS,     // The table to query
                    settingsProjection,                         // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereArgs,                                  // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                        // The sort order
            );

            // Remove all gifts associated to all rooms of the given user
            if (cursor.moveToFirst()) {
                do {
                    String roomId = cursor.getString(cursor.getColumnIndex(DatabaseContract.RoomEntry.KEY_ROOM_ID));
                    db.delete(DatabaseContract.GiftEntry.TABLE_GIFTS, DatabaseContract.GiftEntry.KEY_GIFT_ROOM_ID_FK + " = ?", new String[]{ roomId });
                } while(cursor.moveToNext());
            }
            cursor.close();

            db.delete(DatabaseContract.RoomEntry.TABLE_ROOMS, DatabaseContract.RoomEntry.KEY_ROOM_USER_ID_FK + " = ?", whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to delete all rooms from user " + userId, ignored);
        } finally {
            db.endTransaction();
        }
    }

    public List<Room> getAllRooms(String userId) {
        List<Room> rooms = new ArrayList<>();

        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Double>>(){}.getType();

        SQLiteDatabase db = getReadableDatabase();

        String [] settingsProjection = {
                DatabaseContract.RoomEntry.KEY_ROOM_ID,
                DatabaseContract.RoomEntry.KEY_ROOM_NAME,
                DatabaseContract.RoomEntry.KEY_ROOM_OCCASION
        };
        String [] giftSettingsProjection = {
                DatabaseContract.GiftEntry.KEY_GIFT_ID,
                DatabaseContract.GiftEntry.KEY_GIFT_NAME,
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT,
                DatabaseContract.GiftEntry.KEY_GIFT_PRICE,
                DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER,
                DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION,
                DatabaseContract.GiftEntry.KEY_GIFT_URL,
                DatabaseContract.GiftEntry.KEY_GIFT_IMAGE,
                DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG,
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER
        };

        String giftWhereClause = DatabaseContract.GiftEntry.KEY_GIFT_ROOM_ID_FK + "=?";

        String whereClause = DatabaseContract.RoomEntry.KEY_ROOM_USER_ID_FK + "=?";
        String [] whereArgs = {userId};

        Cursor cursor = db.query(
                DatabaseContract.RoomEntry.TABLE_ROOMS,     // The table to query
                settingsProjection,                         // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                                  // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );

        if (cursor.moveToFirst()) {
            do {
                final String roomId = cursor.getString(cursor.getColumnIndex(DatabaseContract.RoomEntry.KEY_ROOM_ID));

                List<Gift> gifts = new ArrayList<>();
                String [] giftWhereArgs = {roomId};

                Cursor giftCursor = db.query(
                        DatabaseContract.GiftEntry.TABLE_GIFTS,     // The table to query
                        giftSettingsProjection,                     // The columns to return
                        giftWhereClause,                            // The columns for the WHERE clause
                        giftWhereArgs,                              // The values for the WHERE clause
                        null,                                       // don't group the rows
                        null,                                       // don't filter by row groups
                        null                                        // The sort order
                );

                if (giftCursor.moveToFirst()) {
                    do {
                        Gift gift = new Gift(giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_ID)),
                                giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_NAME)),
                                giftCursor.getDouble(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT)),
                                giftCursor.getDouble(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_PRICE)),
                                giftCursor.getDouble(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER)),
                                giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION)),
                                giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_URL)),
                                giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_IMAGE)),
                                giftCursor.getInt(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG)) > 0);
                        Map<String, Double> amoutPerUser =
                                gson.fromJson(giftCursor.getString(giftCursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER)),
                                        mapType);
                        gift.setAmountByUser(amoutPerUser);
                        gifts.add(gift);
                    } while(giftCursor.moveToNext());
                }
                giftCursor.close();

                Room room = new Room(roomId,
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.RoomEntry.KEY_ROOM_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.RoomEntry.KEY_ROOM_OCCASION)),
                        gifts);
                rooms.add(room);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return rooms;
    }

    public boolean roomExists(String roomId) {
        boolean exists = false;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT 1 FROM "
                + DatabaseContract.RoomEntry.TABLE_ROOMS + " WHERE " + DatabaseContract.RoomEntry.KEY_ROOM_ID + "=?", new String[] { roomId });
        try {
        exists = (cursor.getCount() > 0);
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to acknowledge if the room [" + roomId + "] exists.", ignored);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.endTransaction();
        }
        return exists;
    }
    // endregion

    // region User
    // Insert or update a user in the database
    public void addOrUpdateUser(User user) {
        Log.d(TAG, "addOrUpdateUser: trying to add user " + user);

        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.UserEntry.KEY_USER_ID, user.getName());
            values.put(DatabaseContract.UserEntry.KEY_USER_TOKEN, user.getToken());

            // First try to update the user in case the user already exists in the database
            int rows = db.update(DatabaseContract.UserEntry.TABLE_USERS, values, DatabaseContract.UserEntry.KEY_USER_ID + "= ?", new String[]{user.getName()});

            // Check if update succeeded
            if (rows < 1) {
                // user with this username did not already exist, so insert new user
                db.insertOrThrow(DatabaseContract.UserEntry.TABLE_USERS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to add or update user " + user, ignored);
        } finally {
            db.endTransaction();
        }
    }

    public void deleteUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DatabaseContract.GiftEntry.TABLE_GIFTS, null, null);
            db.delete(DatabaseContract.RoomEntry.TABLE_ROOMS, null, null);
            db.delete(DatabaseContract.UserEntry.TABLE_USERS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to delete all rooms and users", ignored);
        } finally {
            db.endTransaction();
        }
    }

    public boolean userExists(String userId) {
        boolean exists = false;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT 1 FROM "
                + DatabaseContract.UserEntry.TABLE_USERS + " WHERE " + DatabaseContract.UserEntry.KEY_USER_ID + "=?", new String[] { userId });
        try {
            exists = (cursor.getCount() > 0);
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to acknowledge if the user " + userId + " exists.", ignored);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.endTransaction();
        }
        return exists;
    }
    // endregion

    // region Gift
    public boolean giftExists(String giftId) {
        boolean exists = false;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT 1 FROM "
                + DatabaseContract.GiftEntry.TABLE_GIFTS + " WHERE " + DatabaseContract.GiftEntry.KEY_GIFT_ID + "=?", new String[] { giftId });
        try {
            exists = (cursor.getCount() > 0);
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to acknowledge if the gift [" + giftId + "] exists.", ignored);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.endTransaction();
        }
        return exists;
    }

    public @Nullable Gift getGift(String giftId) {
        Gift gift = null;

        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Double>>(){}.getType();

        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();

        // Get the primary key of the user we just updated
        db.beginTransaction();

        String [] settingsProjection = {
                DatabaseContract.GiftEntry.KEY_GIFT_ID,
                DatabaseContract.GiftEntry.KEY_GIFT_NAME,
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT,
                DatabaseContract.GiftEntry.KEY_GIFT_PRICE,
                DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER,
                DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION,
                DatabaseContract.GiftEntry.KEY_GIFT_URL,
                DatabaseContract.GiftEntry.KEY_GIFT_IMAGE,
                DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG,
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER
        };

        String whereClause = DatabaseContract.GiftEntry.KEY_GIFT_ID + "=?";
        String [] whereArgs = {giftId};

        Cursor cursor = db.query(
                DatabaseContract.GiftEntry.TABLE_GIFTS,     // The table to query
                settingsProjection,                         // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                                  // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );

        if (cursor.moveToFirst()) {
            gift = new Gift(cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT)),
                    cursor.getDouble(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_PRICE)),
                    cursor.getDouble(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_URL)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_IMAGE)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG)) > 0);
            Map<String, Double> amoutPerUser =
                    gson.fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER)),
                            mapType);
            gift.setAmountByUser(amoutPerUser);
            db.setTransactionSuccessful();
        }
        cursor.close();
        db.endTransaction();
        return gift;
    }

    public void addOrUpdateGift(Gift gift, String roomId) {
        Log.d(TAG, "addOrUpdateGift: trying to add the gift" + gift);

        Gson gson = new Gson();

        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_ID, gift.getId());
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_ROOM_ID_FK, roomId);
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_NAME, gift.getName());
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT, gift.getAmount());
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_PRICE, gift.getPrice());
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER, gift.getRemainder());
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION, gift.getDescription());
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_URL, gift.getUrl());
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_IMAGE, gift.getImageUrl());
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG, gift.isImageFileLocal() ? 1 : 0);
            values.put(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER, gson.toJson(gift.getAmountByUser()));

            // First try to update the gift in case it already exists in the database
            Log.d(TAG, "addOrUpdateGift: Try to update the gift in case it already exists in the database");
            int rows = db.update(DatabaseContract.GiftEntry.TABLE_GIFTS, values, DatabaseContract.GiftEntry.KEY_GIFT_ID + "= ?", new String[]{ gift.getId() });

            // Check if update succeeded
            if (rows < 1) {
                // gift with this id did not already exist, so insert new gift
                Log.d(TAG, "addOrUpdateGift: Gift with this id did not already exist, so insert new gift");
                db.insertOrThrow(DatabaseContract.GiftEntry.TABLE_GIFTS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to add or update the gift " + gift, ignored);
        } finally {
            db.endTransaction();
        }
    }

    public List<Gift> getAllGifts(String roomId) {
        List<Gift> gifts = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Double>>(){}.getType();

        String [] settingsProjection = {
                DatabaseContract.GiftEntry.KEY_GIFT_ID,
                DatabaseContract.GiftEntry.KEY_GIFT_NAME,
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT,
                DatabaseContract.GiftEntry.KEY_GIFT_PRICE,
                DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER,
                DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION,
                DatabaseContract.GiftEntry.KEY_GIFT_URL,
                DatabaseContract.GiftEntry.KEY_GIFT_IMAGE,
                DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG,
                DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER
        };

        String whereClause = DatabaseContract.GiftEntry.KEY_GIFT_ROOM_ID_FK + "=?";
        String [] whereArgs = {roomId};

        Cursor cursor = db.query(
                DatabaseContract.GiftEntry.TABLE_GIFTS,     // The table to query
                settingsProjection,                         // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                                  // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );

        if (cursor.moveToFirst()) {
            do {
                Gift gift = new Gift(cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_REMAINDER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_URL)),
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_IMAGE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_LOCAL_IMAGE_FLAG)) > 0);
                Map<String, Double> amoutPerUser =
                        gson.fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.GiftEntry.KEY_GIFT_AMOUNT_PER_USER)),
                                mapType);
                gift.setAmountByUser(amoutPerUser);
                gifts.add(gift);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return gifts;
    }

    public void deleteGifts(final String roomId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String[] whereArgs = {roomId};
            db.delete(DatabaseContract.GiftEntry.TABLE_GIFTS, DatabaseContract.GiftEntry.KEY_GIFT_ROOM_ID_FK + " = ?", whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            Log.e(TAG, "Error while trying to delete all gifts in room[" + roomId + "]", ignored);
        } finally {
            db.endTransaction();
        }
    }
    // endregion

}
