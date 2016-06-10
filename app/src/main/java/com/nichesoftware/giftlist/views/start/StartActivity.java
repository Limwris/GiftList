package com.nichesoftware.giftlist.views.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.StartContract;
import com.nichesoftware.giftlist.presenters.StartPresenter;
import com.nichesoftware.giftlist.views.rooms.RoomsActivity;

/**
 * Created by n_che on 08/06/2016.
 */
public class StartActivity extends AppCompatActivity implements StartContract.View {
    private static final String TAG = StartActivity.class.getSimpleName();

    private ProgressDialog progressDialog;

    /**
     * Listener sur les actions de l'utilisateur
     */
    private StartContract.UserActionListener actionsListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        actionsListener = new StartPresenter(this, Injection.getDataProvider(this));

        ViewPager viewPager = (ViewPager) findViewById(R.id.didacticiel_pager);
        viewPager.setAdapter(new DidacticielPagerAdapter(this));

        findViewById(R.id.didacticiel_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsListener.startApplication();
            }
        });
    }

    @Override
    public void showRoomsActivity() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showRoomsActivity");
        }
        Intent intent = new Intent(this, RoomsActivity.class);
        startActivity(intent);
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
