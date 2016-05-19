package com.nichesoftware.giftlist.views.giftlist;

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
    public static final String EXTRA_PERSON_ID = "PERSON_ID";

    /**
     * Vue du détail de la note
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
        String personId = getIntent().getStringExtra(EXTRA_PERSON_ID);
        initView(personId);
    }

    /**
     * Initialisation de la vue
     * @param personId
     */
    private void initView(@Nullable final String personId) {
        giftListView.compile(personId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
