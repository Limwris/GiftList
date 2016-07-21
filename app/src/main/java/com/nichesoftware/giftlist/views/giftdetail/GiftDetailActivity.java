package com.nichesoftware.giftlist.views.giftdetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.GiftDetailContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.presenters.GiftDetailPresenter;
import com.nichesoftware.giftlist.views.giftlist.GiftListActivity;

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
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        TextView giftNameTextView = (TextView) findViewById(R.id.gift_detail_name);
        giftNameTextView.setText(gift.getName());
        final EditText giftAmountEditText = (EditText) findViewById(R.id.gift_detail_amount);

        Button modifyButton = (Button) findViewById(R.id.gift_detail_modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    double amount = Double.valueOf(giftAmountEditText.getText().toString());
                    if (amount > gift.getPrice()) {
                        giftAmountEditText.setError(String.format(getString(R.string.gift_detail_too_high_error_text), gift.getPrice()));
                    } else {
                        actionsListener.updateGift(gift, roomId, amount);
                    }
                } catch (NumberFormatException e) {
                    giftAmountEditText.setError(getString(R.string.gift_detail_nan_error_text));
                }


            }
        });
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
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/

    @Override
    public void onUpdateGiftSuccess() {
        Intent intent = new Intent();
        intent.putExtra(GiftListActivity.EXTRA_ROOM_ID, roomId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onUpdateGiftFailed() {
        // Todo
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
