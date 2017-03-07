package com.nichesoftware.giftlist.views.adduser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Contact adapter
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    // Constants   ---------------------------------------------------------------------------------
    public static final String TAG = ContactAdapter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
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
    public ContactAdapter(List<AddUserVO> users, ItemChangeListener itemChangeListener) {
        Log.d(TAG, "ContactsAdapter");
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
        Log.d(TAG, String.format("onBindViewHolder - User [username: %s, phone: %s]",
                vo.getUser().getUsername(), vo.getUser().getPhoneNumber()));

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

    public List<AddUserVO> getList() {
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
        TextView name;
        /**
         * Checkbox de sélection de l'utilisateur
         */
        CheckBox checkBox;
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
