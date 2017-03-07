package com.nichesoftware.giftlist.views.giftlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;

import java.util.List;

/**
 * Gift list detail adapter
 */
public class GiftListDetailAdapter extends RecyclerView.Adapter<GiftListDetailAdapter.ViewHolder> {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftListDetailAdapter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Données (liste de cadeaux)
     */
    private List<GiftListDetailVO> VOs;

    /**
     * Context
     */
    private Context context;

    /**
     * Constructeur
     * @param VOs
     */
    public GiftListDetailAdapter(List<GiftListDetailVO> VOs) {
        Log.d(TAG, "GiftListDetailAdapter");

        this.VOs = VOs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View giftDetailView = inflater.inflate(R.layout.gift_list_detail_item_view, parent, false);

        return new ViewHolder(giftDetailView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        GiftListDetailVO vo = VOs.get(position);

        viewHolder.name.setText(vo.getUsername());
        viewHolder.amount.setText(String.format(context.getResources().getString(R.string.gift_list_detail_amount_description), vo.getParticipation()));
    }

    @Override
    public int getItemCount() {
        return VOs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Nom du cadeau
         */
        public TextView name;

        /**
         * Participation du cadeau
         */
        public TextView amount;


        /**
         * Constructeur
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.gift_list_detail_username);
            amount = (TextView) itemView.findViewById(R.id.gift_list_detail_participation);
        }
    }
}
