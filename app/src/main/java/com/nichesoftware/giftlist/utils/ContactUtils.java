package com.nichesoftware.giftlist.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.nichesoftware.giftlist.BaseApplication;

import java.util.Locale;

/**
 * Utility class for Contacts
 */
public final class ContactUtils {
    // Constants
    private static final String TAG = ContactUtils.class.getSimpleName();

    /**
     * Private constructor
     */
    private ContactUtils() {
        // Nothing
    }

    public static Uri getContactImageUrl(final String phoneNumber) {
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

    private static long getContactIdFromPhoneNumber(final String phoneNumber) {
        Log.d(TAG, "getContactIdFromPhoneNumber");

        final Context context = BaseApplication.getAppContext();
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = contentResolver.query(uri,
                new String[]{ContactsContract.PhoneLookup._ID},
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
}