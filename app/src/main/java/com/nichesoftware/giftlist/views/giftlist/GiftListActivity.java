package com.nichesoftware.giftlist.views.giftlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nichesoftware.giftlist.R;

/**
 * Created by n_che on 25/04/2016.
 */
public class GiftListActivity extends AppCompatActivity {
    public static final String EXTRA_ROOM_ID = "ROOM_ID";
    public static final int ADD_GIFT_REQUEST = 1;  // The request code

    /**
     * Vue de la liste des cadeaux
     */
    private GiftListView giftListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gift_list_activity);
        giftListView = (GiftListView) findViewById(R.id.container);

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

        // Get the requested note id
        int personId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0); // Todo: valeur par défaut ?
        initView(personId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_GIFT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                giftListView.forceReload();
            }
        }
    }

    /**
     * Initialisation de la vue
     * @param personId
     */
    private void initView(@Nullable final int personId) {
        giftListView.compile(personId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
