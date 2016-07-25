package com.nichesoftware.giftlist.views.addgift;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AddGiftContract;
import com.nichesoftware.giftlist.presenters.AddGiftPresenter;
import com.nichesoftware.giftlist.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by n_che on 09/06/2016.
 */
public class AddGiftActivity extends AppCompatActivity implements AddGiftContract.View {
    private static final String TAG = AddGiftActivity.class.getSimpleName();
    public static final String PARCELABLE_ROOM_ID_KEY = "PARCELABLE_ROOM_ID_KEY";
    public static final int ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST = 100;
    public static final int ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST = 101;

    /**
     * Model
     */
    private int roomId;
    private String imagePath;
    /**
     * Graphical components
     */
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText amountEditText;
    private ImageView imageView;
    private Dialog addImageDialog;
    private ProgressDialog progressDialog;

    /*
     * Listener sur les actions de l'utilisateur
     */
    private AddGiftContract.UserActionListener actionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_gift_activity);

        /**
         * Récupération du cadeau
         */
        roomId = getIntent().getIntExtra(PARCELABLE_ROOM_ID_KEY, -1);

        actionsListener = new AddGiftPresenter(this, Injection.getDataProvider(this));

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        nameEditText = (EditText) findViewById(R.id.add_gift_name_edit_text);
        priceEditText = (EditText) findViewById(R.id.add_gift_price_edit_text);
        amountEditText = (EditText) findViewById(R.id.add_gift_amount_edit_text);
        imageView = (ImageView) findViewById(R.id.add_gift_image);

        findViewById(R.id.add_gift_add_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageDialog = new AppCompatDialog(AddGiftActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                addImageDialog.setContentView(R.layout.add_gift_add_image_dialog);
                addImageDialog.findViewById(R.id.add_gift_add_image_select_picture)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectGalleryPicture();
                            }
                        });
                addImageDialog.findViewById(R.id.add_gift_add_image_take_picture)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                takePicture();
                            }
                        });
                addImageDialog.setTitle(getResources().getString(R.string.add_gift_add_image_title_dialog));
                addImageDialog.setCanceledOnTouchOutside(true);
                addImageDialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("onActivityResult - with resultCode: %d and requestCode: %d", resultCode, requestCode));
        }
        if (requestCode == ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST
                && resultCode == AppCompatActivity.RESULT_OK) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onActivityResult - ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST");
            }
            if (addImageDialog != null) {
                addImageDialog.dismiss();
            }
            if (data == null) {
                //Display an error
                return;
            }

            // Getting the Absolute File Path from Content URI
            Uri selectedImage = data.getData();
            imagePath = getRealPathFromURI(selectedImage);
            imageView.setImageURI(selectedImage);
        } else if (requestCode == ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST
                && resultCode == AppCompatActivity.RESULT_OK) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onActivityResult - ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST");
            }
            if (addImageDialog != null) {
                addImageDialog.dismiss();
            }
            setPic(imagePath);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_gift_menu, menu);
        MenuItem item = menu.findItem(R.id.disconnection_menu_item);
        if (actionsListener.isConnected()) {
            item.setEnabled(true);
        } else {
            // disabled
            item.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disconnection_menu_item:
                actionsListener.doDisconnect();
                return true;
            case R.id.add_gift_menu_item:
                doAddGift();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    private void doAddGift() {
        if (!validate()) {
            onCreateGiftFailed();
            return;
        }
        final String name = nameEditText.getText().toString();
        final double price = Double.valueOf(priceEditText.getText().toString());
        final double amount = Double.valueOf(amountEditText.getText().toString());
        actionsListener.addGift(roomId, name, price, amount, imagePath);
    }

    private void selectGalleryPicture() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "selectGalleryPicture");
        }
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.add_gift_add_image_title_dialog));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST);
    }

    private void takePicture() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "takePicture");
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                if(BuildConfig.DEBUG) {
                    Log.e(TAG, "captureImage - Error occurred while creating the File");
                }
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("createImageFile - Getpath : %s", imagePath));
        }
        return image;
    }

    private String getRealPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
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

    private void setPic(final String filepath) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filepath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private boolean validate() {
        boolean valid = true;

        final String roomName = nameEditText.getText().toString();
        if (StringUtils.isEmpty(roomName)) {
            nameEditText.setError(getString(R.string.add_gift_empty_field_error_text));
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        try {
            Double.valueOf(priceEditText.getText().toString());
        } catch (NumberFormatException e) {
            priceEditText.setError(getString(R.string.add_gift_nan_error_text));
            valid = false;
        }

        try {
            Double.valueOf(amountEditText.getText().toString());
        } catch (NumberFormatException e) {
            amountEditText.setError(getString(R.string.add_gift_nan_error_text));
            valid = false;
        }

        return valid;
    }

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/


    @Override
    public void onCreateGiftSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onCreateGiftFailed() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Failed", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void showLoader() {
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    public void hideLoader() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
