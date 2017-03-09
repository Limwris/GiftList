package com.nichesoftware.giftlist.views.giftdetail;

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
import android.widget.TextView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.GiftDetailContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.presenters.GiftDetailPresenter;
import com.nichesoftware.giftlist.utils.PictureUtils;
import com.nichesoftware.giftlist.utils.StringUtils;
import com.nichesoftware.giftlist.views.AbstractActivity;
import com.nichesoftware.giftlist.views.addimage.AddImageDialog;
import com.nichesoftware.giftlist.views.giftlist.GiftListActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Gift detail screen
 */
public class GiftDetailActivity extends AbstractActivity<GiftDetailContract.Presenter>
        implements GiftDetailContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftDetailActivity.class.getSimpleName();
    public static final String PARCELABLE_GIFT_KEY = "PARCELABLE_GIFT_KEY";
    public static final String EXTRA_ROOM_ID = "ROOM_ID";

    // Fields   ------------------------------------------------------------------------------------
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
    @BindView(R.id.gift_detail_image)
    ImageView mGiftImageView;
    @BindView(R.id.gift_detail_description_edit_text)
    EditText mDescriptionEditText;
    @BindView(R.id.gift_detail_amount)
    EditText mGiftAmountEditText;
    @BindView(R.id.gift_detail_name)
    TextView mGiftNameTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_progressBar)
    ProgressBar mProgressBar;
    // Add image dialog
    private Dialog mAddImageDialog;

    @OnClick(R.id.gift_detail_image)
    void onGiftImageClick() {
        mAddImageDialog = new AddImageDialog(this, new AddImageDialog.OnDialogListener() {
            @Override
            public void onSelectPicture() {
                PictureUtils.selectGalleryPicture(GiftDetailActivity.this);
            }

            @Override
            public void onTakePicture() {
                PictureUtils.takePicture(GiftDetailActivity.this, imagePath);
            }
        }, getString(R.string.add_gift_add_image_title_dialog));
        mAddImageDialog.show();
    }

    @OnClick(R.id.gift_detail_modify_button)
    void onModifyButtonClick() {
        try {
            double amount = Double.valueOf(mGiftAmountEditText.getText().toString());
            final String description = mDescriptionEditText.getText().toString();
            if (amount > gift.remainder(presenter.getCurrentUsername())) {
                mGiftAmountEditText.setError(
                        String.format(getString(R.string.gift_detail_too_high_error_text),
                                gift.remainder(presenter.getCurrentUsername()))
                );
            } else {
                if (isImageChanged) {
                    Log.d(TAG, String.format(Locale.getDefault(),
                            "onClick - imageChange: %s", imagePath));
                    presenter.updateGift(gift, roomId, amount, description, imagePath);
                } else {
                    Log.d(TAG, "onClick - image did not change");
                    presenter.updateGift(gift, roomId, amount, description, null);
                }
            }
        } catch (NumberFormatException e) {
            mGiftAmountEditText.setError(getString(R.string.gift_detail_nan_error_text));
        }
    }

    @Override
    protected void initView() {
        super.initView();

        /**
         * Récupération du cadeau
         */
        gift = getIntent().getParcelableExtra(PARCELABLE_GIFT_KEY);
        /**
         * Récupération de l'identifiant de la salle
         */
        roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, -1);

        // Set up the toolbar.
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.gift_detail_title));
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        mGiftNameTextView.setText(gift.getName());
        mGiftAmountEditText.setText(String.valueOf(gift.getAmount()));

        if (!StringUtils.isEmpty(gift.getDescription())) {
            mDescriptionEditText.setText(gift.getDescription());
        }

        Picasso.with(this).load(Injection.getDataProvider(this).getGiftImageUrl(gift.getId()))
                .fit().centerCrop().placeholder(R.drawable.placeholder).into(mGiftImageView);

        // Create the File where the photo should go
        try {
            imagePath = PictureUtils.createImageFile().getAbsolutePath();
        } catch (IOException ignored) {
            // Error occurred while creating the File
            Log.e(TAG, "captureImage - Error occurred while creating the File {}", ignored);
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
        return R.layout.gift_detail_activity;
    }

    @Override
    protected GiftDetailContract.Presenter newPresenter() {
        return new GiftDetailPresenter(this, Injection.getDataProvider(this));
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
            mGiftImageView.setImageURI(selectedImage);
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
        getMenuInflater().inflate(R.menu.standard_menu, menu);
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

    private void setPic(final String filepath) {
        // Get the dimensions of the View
        int targetW = mGiftImageView.getWidth();
        int targetH = mGiftImageView.getHeight();

        Bitmap bitmap = PictureUtils.getBitmap(filepath, targetW, targetH);
        mGiftImageView.setImageBitmap(bitmap);

        PictureUtils.saveBitmap(bitmap, imagePath);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     View contract
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onUpdateGiftSuccess() {
        // Force Picasso to reload data for this file
        final String filePath = Injection.getDataProvider(this).getGiftImageUrl(gift.getId());
        Picasso.with(this).invalidate(filePath);
        Picasso.with(this).load(filePath).fit().centerCrop().into(mGiftImageView);
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
        mProgressBar.animate();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(@NonNull String message) {

    }

    @Override
    protected void performLogin() {

    }
}
