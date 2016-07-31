package com.nichesoftware.giftlist.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Locale;

/**
 * Created by Kattleya on 27/07/2016.
 */
public final class FileUtils {
    private static final String TAG = FileUtils.class.getCanonicalName();
    public static final String JPEG_EXTENSION = ".jpg";
    private static final String CONTENT_URI_FILE = "file://%s";

    /**
     * Constructeur par défaut
     */
    private FileUtils() {
        // Do nothing
    }

    /**
     * @param context
     * @param fileName
     * @return the path of the file, null if the file is not found
     */
    public static String getPathOfFileName(@NonNull Context context, @NonNull String fileName) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getPathOfFileName: %s", fileName));
        }

        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + fileName);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getPathOfFileName [path: %s, size: %d]",
                    file.getAbsolutePath(), file.length()));
        }
        return file.getAbsolutePath();
    }

    public static String getContentUriFileName(@NonNull Context context, @NonNull String fileName) {
        return String.format(Locale.getDefault(), CONTENT_URI_FILE, getPathOfFileName(context, fileName));
    }

    /**
     * @param context
     * @return the path of the file, null if the file is not found
     */
    public static String getDirectory(@NonNull Context context ) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getPath");
        }

        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getPath [directory: %s]", file.getAbsolutePath()));
        }
        return file.getAbsolutePath();
    }

    public static void copy(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if (sourceChannel != null) { sourceChannel.close(); }
            if (destChannel != null) { destChannel.close(); }
        }
    }

    public static boolean remove(File file) throws IOException {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "remove");
        }
        if (file.exists()) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "remove - file exists");
            }
            return file.delete();
        } else {
            return false;
        }
    }

    public static boolean remove(final String filePath) throws IOException {
        return remove(new File(filePath));
    }

    public static void savePicToLocalFolder(final String tempFile, final String newFile) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(Locale.getDefault(),
                    "savePicToLocalFolder [current file: %s, new file: %s]", tempFile, newFile));
        }
        File from = new File(tempFile);
        File to = new File(newFile);

        if (!from.renameTo(to) || to.length() == 0) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "savePicToLocalFolder - rename failed...");
            }
            try {
                FileUtils.copy(from, to);
            } catch (IOException ignored) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "savePicToLocalFolder - copy failed {}", ignored);
                }
                return; // Do not log succeed
            }
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(Locale.getDefault(),
                    "savePicToLocalFolder [new file path: %s, size: %d]", to.getAbsolutePath(), to.length()));
        }
//        Uri contentUri = Uri.fromFile(to);
//
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
//        context.sendBroadcast(mediaScanIntent);
//        if (BuildConfig.DEBUG) {
//            Log.d(TAG, String.format(Locale.getDefault(),
//                    "savePicToLocalFolder - Intent sent [contentUri: %s]", contentUri.toString()));
//        }
    }
}
