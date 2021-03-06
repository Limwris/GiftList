package com.nichesoftware.giftlist.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kattleya on 26/07/2016.
 */
public final class PictureUtils {
    private static final String TAG = PictureUtils.class.getSimpleName();
    public static final int ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST = 100;
    public static final int ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST = 101;

    private PictureUtils() {
        // Nothing
    }

    public static void selectGalleryPicture(AppCompatActivity activity) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "selectGalleryPicture");
        }
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, activity.getString(R.string.add_gift_add_image_title_dialog));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        activity.startActivityForResult(chooserIntent, ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST);
    }

    public static void takePicture(AppCompatActivity activity, final String filePath) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "takePicture");
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = new File(filePath);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            activity.startActivityForResult(takePictureIntent, ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST);
        }
    }

    public static String getRealPathFromURI(final Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(Locale.getDefault(),
                    "getRealPathFromURI [Uri: %s]", uri.toString()));
        }
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            return picturePath;
        } else {
            return uri.getPath();
        }
    }

//    public static void copy(File source, File destination) {
//
//        FileChannel in;
//        FileChannel out;
//        try {
//            in = new FileInputStream(source).getChannel();
//            out = new FileOutputStream(destination).getChannel();
//        } catch (FileNotFoundException e) {
//            if (BuildConfig.DEBUG) {
//                Log.e(TAG, "Error when retrieving files {}.", e);
//            }
//            return;
//        }
//
//        try {
//            in.transferTo(0, in.size(), out);
//        } catch(Exception ignored) {
//            // post to log
//        } finally {
//            try { if(in != null) in.close(); } catch (IOException ignored) { }
//            try { if(out != null) out.close(); } catch (IOException ignored) { }
//        }
//    }

    public static File createImageFile() throws IOException {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "createImageFile");
        }
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(Locale.getDefault(),
                    "createImageFile [directoryPath: %s, imageName: %s]",
                    storageDir.getAbsolutePath(), imageFileName));
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("createImageFile - complete path : %s", image.getAbsolutePath()));
        }
        return image;
    }
}
