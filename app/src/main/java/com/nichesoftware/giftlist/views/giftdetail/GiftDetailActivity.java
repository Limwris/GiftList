package com.nichesoftware.giftlist.views.giftdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.GiftDetailContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.presenters.GiftDetailPresenter;
import com.nichesoftware.giftlist.utils.PictureUtils;
import com.nichesoftware.giftlist.utils.StringUtils;
import com.nichesoftware.giftlist.views.giftlist.GiftListActivity;
import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by n_che on 09/06/2016.
 */
public class GiftDetailActivity extends AppCompatActivity implements GiftDetailContract.View {
    private static final String TAG = GiftDetailActivity.class.getSimpleName();
    public static final String PARCELABLE_GIFT_KEY = "PARCELABLE_GIFT_KEY";
    public static final String EXTRA_ROOM_ID = "ROOM_ID";

    /**
     * Model
     */
    private Gift gift;
    private int roomId;
    private String imagePath;
    private boolean isImageChanged;

    /**
     * Graphical components
     */
    private ImageView giftImageView;
    private EditText descriptionEditText;
    private Dialog addImageDialog;

    /*
     * Listener sur les actions de l'utilisateur
     */
    private GiftDetailContract.UserActionListener actionsListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gift_detail_activity);

        /**
         * Récupération du cadeau
         */
        gift = getIntent().getParcelableExtra(PARCELABLE_GIFT_KEY);
        /**
         * Récupération de l'identifiant de la salle
         */
        roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, -1);

        actionsListener = new GiftDetailPresenter(this, Injection.getDataProvider(this));

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.gift_detail_title));
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        TextView giftNameTextView = (TextView) findViewById(R.id.gift_detail_name);
        giftNameTextView.setText(gift.getName());
        final EditText giftAmountEditText = (EditText) findViewById(R.id.gift_detail_amount);
        giftAmountEditText.setText(String.valueOf(gift.getAmount()));
        giftImageView = (ImageView) findViewById(R.id.gift_detail_image);
        descriptionEditText = (EditText) findViewById(R.id.gift_detail_description_edit_text);
        if (!StringUtils.isEmpty(gift.getDescription())) {
            descriptionEditText.setText(gift.getDescription());
        }

        Picasso.with(this).load(Injection.getDataProvider(this).getGiftImageUrl(gift.getId()))
                .fit().centerCrop().placeholder(R.drawable.placeholder).into(giftImageView);

        // Create the File where the photo should go
        try {
            imagePath = PictureUtils.createImageFile().getAbsolutePath();

            giftImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addImageDialog = new AppCompatDialog(GiftDetailActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    addImageDialog.setContentView(R.layout.add_gift_add_image_dialog);
                    addImageDialog.findViewById(R.id.add_gift_add_image_select_picture)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PictureUtils.selectGalleryPicture(GiftDetailActivity.this);
                                }
                            });
                    addImageDialog.findViewById(R.id.add_gift_add_image_take_picture)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PictureUtils.takePicture(GiftDetailActivity.this, imagePath);
                                }
                            });
                    addImageDialog.setTitle(getResources().getString(R.string.add_gift_add_image_title_dialog));
                    addImageDialog.setCanceledOnTouchOutside(true);
                    addImageDialog.show();
                }
            });
        } catch (IOException ignored) {
            // Error occurred while creating the File
            if(BuildConfig.DEBUG) {
                Log.e(TAG, "captureImage - Error occurred while creating the File {}", ignored);
            }
        }

        Button modifyButton = (Button) findViewById(R.id.gift_detail_modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    double amount = Double.valueOf(giftAmountEditText.getText().toString());
                    final String description = descriptionEditText.getText().toString();
                    if (amount > gift.remainder(actionsListener.getCurrentUsername())) {
                        giftAmountEditText.setError(
                                String.format(getString(R.string.gift_detail_too_high_error_text),
                                        gift.remainder(actionsListener.getCurrentUsername()))
                        );
                    } else {
                        if (isImageChanged) {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, String.format(Locale.getDefault(),
                                        "onClick - imageChange: %s", imagePath));
                            }
                            actionsListener.updateGift(gift, roomId, amount, description, imagePath);
                        } else {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "onClick - image did not change");
                            }
                            actionsListener.updateGift(gift, roomId, amount, description, null);
                        }
                    }
                } catch (NumberFormatException e) {
                    giftAmountEditText.setError(getString(R.string.gift_detail_nan_error_text));
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("onActivityResult - with resultCode: %d and requestCode: %d", resultCode, requestCode));
        }
        if (requestCode == PictureUtils.ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST
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
            imagePath = PictureUtils.getRealPathFromURI(getContext(), selectedImage);
            giftImageView.setImageURI(selectedImage);
            isImageChanged = true;
        } else if (requestCode == PictureUtils.ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST
                && resultCode == AppCompatActivity.RESULT_OK) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onActivityResult - ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST");
            }
            if (addImageDialog != null) {
                addImageDialog.dismiss();
            }
            setPic(imagePath);
            isImageChanged = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.standard_menu, menu);
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
        if (addImageDialog != null) {
            addImageDialog.dismiss();
        }
        super.onDestroy();
    }

    private void setPic(final String filepath) {
        // Get the dimensions of the View
        int targetW = giftImageView.getWidth();
        int targetH = giftImageView.getHeight();

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
        giftImageView.setImageBitmap(bitmap);

        // Todo: mettre l'image à la bonne taille (400px x 400 px)
        OutputStream fOut = null;
        try {
            fOut = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        } finally {
            if (fOut != null) try { fOut.close(); } catch (IOException ignored) { } // do not forget to close the stream
        }
    }

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/

    @Override
    public void onUpdateGiftSuccess() {
        // Force Picasso to reload data for this file
        final String filePath = Injection.getDataProvider(this).getGiftImageUrl(gift.getId());
        Picasso.with(this).invalidate(filePath);
        Picasso.with(this).load(filePath).fit().centerCrop().into(giftImageView);
        Intent intent = new Intent();
        intent.putExtra(GiftListActivity.EXTRA_ROOM_ID, roomId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onUpdateGiftFailed() {
        // Todo
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Failed", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void showLoader() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.toolbar_progressBar);
        progressBar.animate();
        findViewById(R.id.toolbar_progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        findViewById(R.id.toolbar_progressBar).setVisibility(View.INVISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
