package com.nichesoftware.giftlist.views.adduser;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.utils.ContactUtils;
import com.nichesoftware.giftlist.views.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Contact adapter
 */
/* package */ class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    // Constants   ---------------------------------------------------------------------------------
    public static final String TAG = ContactAdapter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Données (liste de users)
     */
    private List<AddUserVO> mUsers = new ArrayList<>();
    /**
     * Listener on item state changed
     */
    private ItemChangeListener mItemChangeListener;

    /**
     * Constructeur
     *
     * @param itemChangeListener    Listener {@link ItemChangeListener} when item check changed
     */
    /* package */ ContactAdapter(ItemChangeListener itemChangeListener) {
        Log.d(TAG, "ContactsAdapter");
        mItemChangeListener = itemChangeListener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.contacts_item_view, parent, false);

        return new ContactViewHolder(itemView, mItemChangeListener);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder viewHolder, int position) {
        final AddUserVO vo = mUsers.get(position);
        Log.d(TAG, String.format("onBindViewHolder - User [username: %s, phone: %s]",
                vo.getUser().getName(), vo.getUser().getPhoneNumber()));
        viewHolder.bind(vo);
    }

    /* package */ void replaceData(List<AddUserVO> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    public List<AddUserVO> getData() {
        return mUsers;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    // region Inner class
    /**
     * If an item state has been changed (checked, or not checked)
     */
    interface ItemChangeListener {
        void onItemChanged();
    }

    /* package */ static class ContactViewHolder extends ViewHolder<AddUserVO> {

        /**
         * Nom de l'utilisateur
         */
        @BindView(R.id.add_user_contact_name)
        TextView name;
        /**
         * Checkbox de sélection de l'utilisateur
         */
        @BindView(R.id.add_user_checkbox)
        CheckBox checkBox;
        /**
         * Badge
         */
        @BindView(R.id.add_user_badge)
        CircleImageView contactBadge;

        /**
         * Listener on selection change
         */
        private final ItemChangeListener mItemChangeListener;

        /**
         * Default constructor
         *
         * @param itemView Root view of the {@link ViewHolder}
         * @param itemChangeListener Listener on selection change
         */
        /* package */ ContactViewHolder(View itemView, ItemChangeListener itemChangeListener) {
            super(itemView);
            mItemChangeListener = itemChangeListener;
        }

        public void bind(AddUserVO vo) {

            name.setText(vo.getUser().getName());

            // in some cases, it will prevent unwanted situations
            checkBox.setOnCheckedChangeListener(null);

            checkBox.setChecked(vo.isChecked());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                vo.setIsChecked(isChecked);
                if (mItemChangeListener != null) {
                    mItemChangeListener.onItemChanged();
                }
            });

            Picasso.with(contactBadge.getContext())
                    .load(ContactUtils.getContactImageUrl(vo.getUser().getPhoneNumber()))
                    .placeholder(R.drawable.person_placeholder).into(contactBadge);
        }
    }
    // endregion
}
