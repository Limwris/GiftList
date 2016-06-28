package com.nichesoftware.giftlist.views.addgift;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AddGiftContract;
import com.nichesoftware.giftlist.presenters.AddGiftPresenter;
import com.nichesoftware.giftlist.utils.StringUtils;

/**
 * Created by n_che on 09/06/2016.
 */
public class AddGiftActivity extends AppCompatActivity implements AddGiftContract.View {
    private static final String TAG = AddGiftActivity.class.getSimpleName();
    public static final String PARCELABLE_ROOM_ID_KEY = "PARCELABLE_ROOM_ID_KEY";

    /**
     * Model
     */
    private int roomId;
    /**
     * Graphical components
     */
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText amountEditText;
    private AppCompatButton button;
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
        button = (AppCompatButton) findViewById(R.id.add_gift_create_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    onCreateGiftFailed();
                    return;
                }
                final String name = nameEditText.getText().toString();
                final double price = Double.valueOf(priceEditText.getText().toString());
                final double amount = Double.valueOf(amountEditText.getText().toString());
                actionsListener.addGift(roomId, name, price, amount);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        actionsListener.doDisconnect();
        super.onDestroy();
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
