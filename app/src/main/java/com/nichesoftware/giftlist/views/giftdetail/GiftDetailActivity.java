package com.nichesoftware.giftlist.views.giftdetail;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.GiftDetailContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.presenters.GiftDetailPresenter;

/**
 * Created by n_che on 09/06/2016.
 */
public class GiftDetailActivity extends AppCompatActivity implements GiftDetailContract.View {
    private static final String TAG = GiftDetailActivity.class.getSimpleName();
    public static final String PARCELABLE_GIFT_KEY = "PARCELABLE_GIFT_KEY";
    /**
     * Model
     */
    private Gift gift;
    /**
     * Graphical components
     */
    private ProgressDialog progressDialog;

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

        actionsListener = new GiftDetailPresenter(this, Injection.getDataProvider(this));

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
    }

    @Override
    public void onUpdateGiftSuccess() {

    }

    @Override
    public void onUpdateGiftFailed() {

    }

    @Override
    public void showLoader(final String message) {
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideLoader() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
