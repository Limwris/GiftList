package com.nichesoftware.giftlist.database;

import android.provider.BaseColumns;

/**
 * Contracts of the database
 */
/* package */ final class DatabaseContract {
    // Database Info
    /* package */ static final String DATABASE_NAME = "GiftsDatabase";
    /* package */ static final int DATABASE_VERSION = 7;

    // Queries
    /* package */ static final String DROP_QUERY = "DROP TABLE IF EXISTS ";

    /**
     * Private constructor
     */
    private DatabaseContract() {}

    /* package */ static class UserEntry implements BaseColumns {
        // Table Names
        /* package */ static final String TABLE_USERS = "users";

        // User Table Columns
        /* package */ static final String KEY_USER_ID = "username";
        /* package */ static final String KEY_USER_TOKEN = "token";
        /* package */ static final String KEY_USER_TOKEN_SENT = "isTokenSent";
    }

    /* Inner class that defines the table contents */
    /* package */ static class RoomEntry implements BaseColumns{
        /* package */ static final String TABLE_ROOMS = "rooms";

        // Room Table Columns
        /* package */ static final String KEY_ROOM_ID = "id";
        /* package */ static final String KEY_ROOM_USER_ID_FK = "userId";
        /* package */ static final String KEY_ROOM_NAME = "roomName";
        /* package */ static final String KEY_ROOM_OCCASION = "occasion";
    }

    /* Inner class that defines the table contents */
    /* package */ static class GiftEntry implements BaseColumns {
        /* package */ static final String TABLE_GIFTS = "gifts";

        // Gift Table Columns
        /* package */ static final String KEY_GIFT_ID = "id";
        /* package */ static final String KEY_GIFT_ROOM_ID_FK = "roomId";
        /* package */ static final String KEY_GIFT_PRICE = "price";
        /* package */ static final String KEY_GIFT_REMAINDER = "remainder";
        /* package */ static final String KEY_GIFT_NAME = "name";
        /* package */ static final String KEY_GIFT_AMOUNT = "amount";
        /* package */ static final String KEY_GIFT_DESCRIPTION = "description";
        /* package */ static final String KEY_GIFT_URL = "url";
        /* package */ static final String KEY_GIFT_IMAGE = "imageUrl";
        /* package */ static final String KEY_GIFT_LOCAL_IMAGE_FLAG = "isLocalImage";
        /* package */ static final String KEY_GIFT_AMOUNT_PER_USER = "amountPerUser";
    }
}
