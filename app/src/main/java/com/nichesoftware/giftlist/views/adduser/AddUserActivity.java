package com.nichesoftware.giftlist.views.adduser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AddUserContract;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.presenters.AddUserPresenter;
import com.nichesoftware.giftlist.views.ErrorView;
import com.nichesoftware.giftlist.views.giftlist.GiftListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
         * Récupération de l'identifiant de la salle
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
                getSupportActionBar().setTitle(getString(R.string.add_user_title));
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.add_user_recycler_view);
        adapter = new ContactsAdapter(new ArrayList<AddUserVO>(0), new ContactsAdapter.ItemChangeListener() {
            @Override
            public void onItemChanged() {
                invalidateOptionsMenu();
            }
        });
        recyclerView.setAdapter(adapter);
        int numColumns = getResources().getInteger(R.integer.num_contacts_columns);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));

        actionsListener.loadContacts(roomId);
    }

    private void doInviteUsers() {
        for (AddUserVO vo : adapter.getList()) {
            if (vo.isChecked()) {
                actionsListener.inviteUserToCurrentRoom(roomId, vo.getUser().getUsername());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_user_menu, menu);
        MenuItem disconnectionItem = menu.findItem(R.id.disconnection_menu_item);
        MenuItem addItem = menu.findItem(R.id.add_user_menu_item);
        if (actionsListener.isConnected()) {
            disconnectionItem.setEnabled(true);
        } else {
            // disabled
            disconnectionItem.setEnabled(false);
        }
        // Disable addItem by default (no item has been checked)
        addItem.setEnabled(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem addItem = menu.findItem(R.id.add_user_menu_item);
        if (isItemChecked()) {
            addItem.setEnabled(true);
        } else {
            // disabled
            addItem.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private boolean isItemChecked() {
        for (AddUserVO vo : adapter.getList()) {
            if (vo.isChecked()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disconnection_menu_item:
                actionsListener.doDisconnect();
                return true;
            case R.id.add_user_menu_item:
                doInviteUsers();
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
        super.onDestroy();
    }

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/

    @Override
    public void onUserAddedSuccess() {
        Intent intent = new Intent();
        intent.putExtra(GiftListActivity.EXTRA_ROOM_ID, roomId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onUserAddedFailed() {
        // Todo
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Failed", Snackbar.LENGTH_SHORT);
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
    public void showContacts(List<User> users) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showContacts");
        }

        ErrorView errorView = (ErrorView) findViewById(R.id.error_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.add_user_recycler_view);
        if (users == null || users.isEmpty()) {
            errorView.setMessage(getResources().getString(R.string.add_user_error_view_message));
            errorView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            errorView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            List<AddUserVO> vos = new ArrayList<>();
            for (User user : users) {
                AddUserVO vo = new AddUserVO();
                vo.setUser(user);
                vos.add(vo);
            }
            adapter.replaceData(vos);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**********************************************************************************************/
    /********************************     Adapter & ViewHolder     ********************************/
    /**********************************************************************************************/

    private static class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

        /**
         * Données (liste de users)
         */
        private List<AddUserVO> users;

        /**
         * Context
         */
        private Context context;

        /**
         * If an item state has been changed (checked, or not checked)
         */
        interface ItemChangeListener {
            void onItemChanged();
        }
        private ItemChangeListener itemChangeListener;

        /**
         * Constructeur
         * @param users
         */
        public ContactsAdapter(List<AddUserVO> users, ItemChangeListener itemChangeListener) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "ContactsAdapter");
            }
            setList(users);
            this.itemChangeListener = itemChangeListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.contacts_item_view, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final AddUserVO vo = users.get(position);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, String.format("onBindViewHolder - User [username: %s, phone: %s]",
                        vo.getUser().getUsername(), vo.getUser().getPhoneNumber()));
            }

            viewHolder.name.setText(vo.getUser().getUsername());

            // in some cases, it will prevent unwanted situations
            viewHolder.checkBox.setOnCheckedChangeListener(null);

            viewHolder.checkBox.setChecked(vo.isChecked());
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    vo.setIsChecked(isChecked);
                    if (itemChangeListener != null) {
                        itemChangeListener.onItemChanged();
                    }
                }
            });

            Picasso.with(context)
                    .load(Injection.getDataProvider(context).getContactImageUrl(vo.getUser().getPhoneNumber()))
                    .placeholder(R.drawable.person_placeholder).into(viewHolder.contactBadge);
        }

        public void replaceData(List<AddUserVO> users) {
            setList(users);
            notifyDataSetChanged();
        }

        private void setList(List<AddUserVO> users) {
            this.users = users;
        }

        private List<AddUserVO> getList() {
            return this.users;
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public AddUserVO getItem(int position) {
            return users.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            /**
             * Nom de l'utilisateur
             */
            public TextView name;
            /**
             * Checkbox de sélection de l'utilisateur
             */
            public CheckBox checkBox;
            /**
             * Badge
             */
            CircleImageView contactBadge;


            /**
             * Constructeur
             * @param itemView
             */
            public ViewHolder(View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.add_user_contact_name);
                checkBox = (CheckBox) itemView.findViewById(R.id.add_user_checkbox);
                contactBadge = (CircleImageView) itemView.findViewById(R.id.add_user_badge);
            }
        }
    }
}
