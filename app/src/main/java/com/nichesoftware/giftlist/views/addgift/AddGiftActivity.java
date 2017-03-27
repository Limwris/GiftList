package com.nichesoftware.giftlist.views.addgift;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AddGiftContract;
import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.presenters.AddGiftPresenter;
import com.nichesoftware.giftlist.repository.cache.GiftCache;
import com.nichesoftware.giftlist.repository.cache.UserCache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.GiftCloudDataSource;
import com.nichesoftware.giftlist.repository.provider.AuthDataSourceProvider;
import com.nichesoftware.giftlist.session.SessionManager;
import com.nichesoftware.giftlist.utils.PictureUtils;
import com.nichesoftware.giftlist.utils.StringUtils;
import com.nichesoftware.giftlist.views.AuthenticationActivity;
import com.nichesoftware.giftlist.views.addimage.AddImageDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Add gift screen
 */
public class AddGiftActivity extends AuthenticationActivity<AddGiftContract.Presenter>
        implements AddGiftContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddGiftActivity.class.getSimpleName();
    public static final String PARCELABLE_ROOM_ID_KEY = "PARCELABLE_ROOM_ID_KEY";

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Model
     */
    private String mRoomId;
    private String imagePath;
    private boolean isImageChanged;

    /**
     * Graphical components
     */
    @BindView(R.id.add_gift_name_edit_text)
    EditText mNameEditText;
    @BindView(R.id.add_gift_price_edit_text)
    EditText mPriceEditText;
    @BindView(R.id.add_gift_amount_edit_text)
    EditText mAmountEditText;
    @BindView(R.id.add_gift_description_edit_text)
    EditText descriptionEditText;
    @BindView(R.id.add_gift_image)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_progressBar)
    ProgressBar mProgressBar;
    // Add image dialog
    private Dialog mAddImageDialog;

    @OnClick(R.id.add_gift_add_image_button)
    void onAddGiftButtonClick() {
        mAddImageDialog = new AddImageDialog(this, new AddImageDialog.OnDialogListener() {
            @Override
            public void onSelectPicture() {
                PictureUtils.selectGalleryPicture(AddGiftActivity.this);
            }

            @Override
            public void onTakePicture() {
                PictureUtils.takePicture(AddGiftActivity.this, imagePath);
            }
        }, getString(R.string.add_gift_add_image_title_dialog));
        mAddImageDialog.show();
    }

    @Override
    protected void initView() {
        super.initView();

        // Set up the toolbar.
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.add_gift_title));
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        // Create the File where the photo should go
        try {
            imagePath = PictureUtils.createImageFile().getAbsolutePath();
        } catch (IOException e) {
            // Error occurred while creating the File
            Log.e(TAG, "captureImage - Error occurred while creating the File {}", e);
        }
    }

    @Override
    protected void onDestroy() {
        if (mAddImageDialog != null) {
            mAddImageDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected int getContentView() {
        return R.layout.add_gift_activity;
    }

    @Override
    protected AddGiftContract.Presenter newPresenter() {
        // Récupération du cadeau
        mRoomId = getIntent().getStringExtra(PARCELABLE_ROOM_ID_KEY);

        GiftCache cache = new GiftCache(DatabaseManager.getInstance(), mRoomId);
        GiftCloudDataSource cloudDataSource = new GiftCloudDataSource(SessionManager.getInstance().getToken(),
                Injection.getService(), mRoomId);
        UserCache userCache = new UserCache(DatabaseManager.getInstance());
        AuthDataSource authDataSource = new AuthDataSourceProvider(userCache, Injection.getService());
        return new AddGiftPresenter(this, cache, cloudDataSource, authDataSource);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, String.format("onActivityResult - with resultCode: %d and requestCode: %d", resultCode, requestCode));

        if (requestCode == PictureUtils.ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST
                && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d(TAG, "onActivityResult - ADD_GIFT_ADD_GALLERY_IMAGE_REQUEST");
            if (mAddImageDialog != null) {
                mAddImageDialog.dismiss();
            }
            if (data == null) {
                //Display an error
                return;
            }

            // Getting the Absolute File Path from Content URI
            Uri selectedImage = data.getData();
            imagePath = PictureUtils.getRealPathFromURI(this, selectedImage);
            imageView.setImageURI(selectedImage);
            isImageChanged = true;
        } else if (requestCode == PictureUtils.ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST
                && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d(TAG, "onActivityResult - ADD_GIFT_ADD_CAMERA_IMAGE_REQUEST");
            if (mAddImageDialog != null) {
                mAddImageDialog.dismiss();
            }
            setPic(imagePath);
            isImageChanged = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_gift_menu, menu);
        MenuItem item = menu.findItem(R.id.disconnection_menu_item);
        if (presenter.isConnected()) {
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
                presenter.doDisconnect();
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void doAddGift() {
        if (!validate()) {
            onCreateGiftFailed();
            return;
        }
        final String name = mNameEditText.getText().toString();
        final double price = Double.valueOf(mPriceEditText.getText().toString());
        final double amount = Double.valueOf(mAmountEditText.getText().toString());
        final String description = descriptionEditText.getText().toString();
        if (isImageChanged) {
            presenter.addGift(mRoomId, name, price, amount, description, imagePath);
        } else {
            presenter.addGift(mRoomId, name, price, amount, description, null);
        }
    }

    private void setPic(final String filepath) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        Bitmap bitmap = PictureUtils.getBitmap(filepath, targetW, targetH);
        imageView.setImageBitmap(bitmap);

        PictureUtils.saveBitmap(bitmap, imagePath);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean validate() {
        boolean valid = true;

        final String roomName = mNameEditText.getText().toString();
        if (StringUtils.isEmpty(roomName)) {
            mNameEditText.setError(getString(R.string.add_gift_empty_field_error_text));
            valid = false;
        } else {
            mNameEditText.setError(null);
        }

        try {
            Double.valueOf(mPriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            mPriceEditText.setError(getString(R.string.add_gift_nan_error_text));
            valid = false;
        }

        try {
            Double.valueOf(mAmountEditText.getText().toString());
        } catch (NumberFormatException e) {
            mAmountEditText.setError(getString(R.string.add_gift_nan_error_text));
            valid = false;
        }

        return valid;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     View contract
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreateGiftSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onCreateGiftFailed() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Failed", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    protected void performLogin() {
        // Todo
    }

    @Override
    public void showLoader() {
        mProgressBar.animate();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(@NonNull String message) {
        // Todo
    }
}
