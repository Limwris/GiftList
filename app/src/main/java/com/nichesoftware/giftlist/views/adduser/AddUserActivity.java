package com.nichesoftware.giftlist.views.adduser;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AddUserContract;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.presenters.AddUserPresenter;
import com.nichesoftware.giftlist.views.ErrorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 22/06/2016.
 */
public class AddUserActivity extends AppCompatActivity implements AddUserContract.View {
    private static final String TAG = AddUserActivity.class.getSimpleName();
    public static final String EXTRA_ROOM_ID = "ROOM_ID";

    /**
     * Model
     */
    private int roomId;

    /**
     * Adapter lié à la RecyclerView
     */
    private ContactsAdapter adapter;

    /*
     * Listener sur les actions de l'utilisateur
     */
    private AddUserContract.UserActionListener actionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_user_activity);

        /**
         * Récupération du cadeau
         */
        roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, -1);

        actionsListener = new AddUserPresenter(this, Injection.getDataProvider(this));

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

        findViewById(R.id.add_user_invite_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> selectedUsers = getSelectedUsers();
                for (User user : selectedUsers) {
                    actionsListener.inviteUserToCurrentRoom(roomId, user.getUsername());
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new ContactsAdapter(new ArrayList<User>(0));
        recyclerView.setAdapter(adapter);
        int numColumns = getResources().getInteger(R.integer.num_contacts_columns);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));

        actionsListener.loadContacts();
    }

    private List<User> getSelectedUsers() {
        return null;
    }

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/

    @Override
    public void onUserAddedSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onUserAddedFailed() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Failed", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void showLoader(String message) {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showContacts(List<User> users) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showContacts");
        }

        ErrorView errorView = (ErrorView) findViewById(R.id.error_view);
        LinearLayout container = (LinearLayout) findViewById(R.id.main_view);
        if (users == null || users.isEmpty()) {
            errorView.setMessage(getResources().getString(R.string.add_user_error_view_message));
            errorView.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        } else {
            errorView.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
            adapter.replaceData(users);
        }
    }

    /**********************************************************************************************/
    /********************************     Adapter & ViewHolder     ********************************/
    /**********************************************************************************************/

    private static class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

        /**
         * Données (liste de users)
         */
        private List<User> users;

        /**
         * Context
         */
        private Context context;

        /**
         * Constructeur
         * @param users
         */
        public ContactsAdapter(List<User> users) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "ContactsAdapter");
            }
            setList(users);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.contacts_item_view, parent, false);

            return new ViewHolder(noteView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            User gift = users.get(position);

            viewHolder.name.setText(gift.getUsername());
        }

        public void replaceData(List<User> users) {
            setList(users);
            notifyDataSetChanged();
        }

        private void setList(List<User> users) {
            this.users = users;
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public User getItem(int position) {
            return users.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            /**
             * Nom de l'utilisateur
             */
            public TextView name;


            /**
             * Constructeur
             * @param itemView
             */
            public ViewHolder(View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.add_user_contact_name);
            }
        }
    }
}
