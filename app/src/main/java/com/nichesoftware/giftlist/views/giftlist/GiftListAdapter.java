package com.nichesoftware.giftlist.views.giftlist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.model.Gift;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gift list adapter
 */
public class GiftListAdapter extends RecyclerView.Adapter<GiftListAdapter.ViewHolder> {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftListAdapter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Données (liste de cadeaux)
     */
    private List<Gift> gifts;

    /**
     * Listener sur le clic d'un cadeau
     */
    private GiftListActivity.GiftItemListener giftItemListener;

    /**
     * Context
     */
    private Context context;

    /**
     * Position de l'élément où sont affichées les informations supplémentaires
     */
    private int expandedPosition;

    /**
     * Constructeur
     * @param gifts
     */
    public GiftListAdapter(List<Gift> gifts, GiftListActivity.GiftItemListener giftItemListener) {
        Log.d(TAG, "GiftListAdapter");

        expandedPosition = -1;
        setList(gifts);
        this.giftItemListener = giftItemListener;
        Log.d(TAG, "GiftListAdapter - itemListener: " + giftItemListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View giftView = inflater.inflate(R.layout.gift_list_item_view, parent, false);

        return new ViewHolder(giftView, giftItemListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Gift gift = gifts.get(position);
        final List<GiftListDetailVO> VOs = new ArrayList<>();
        double remainder = gift.getPrice();
        for (Map.Entry<String, Double> entry : gift.getAmountByUser().entrySet()) {
            GiftListDetailVO vo = new GiftListDetailVO();
            vo.setUsername(entry.getKey());
            double participation = entry.getValue();
            vo.setParticipation(participation);
            VOs.add(vo);

            remainder-=participation;
        }

        final boolean isExpanded = (viewHolder.getAdapterPosition()==expandedPosition);
        if (!isExpanded) {
            viewHolder.detailRecyclerView.setVisibility(View.GONE);
        }

        viewHolder.name.setText(gift.getName());
        viewHolder.price.setText(String.format(context.getResources().getString(R.string.gift_price_description), gift.getPrice()));
        viewHolder.amount.setText(String.format(context.getResources().getString(R.string.gift_amount_description), gift.getAmount()));
        viewHolder.remainder.setText(String.format(context.getResources().getString(R.string.gift_remainder_description), remainder));
        viewHolder.seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.detailRecyclerView.setAdapter(new GiftListDetailAdapter(VOs));
                viewHolder.detailRecyclerView.setVisibility(View.VISIBLE);
                expandedPosition = isExpanded ? -1:viewHolder.getAdapterPosition();
                TransitionManager.beginDelayedTransition(viewHolder.detailRecyclerView);
                notifyDataSetChanged();
            }
        });

        Picasso.with(context)
                .load(Injection.getDataProvider(context).getGiftImageUrl(gift.getId()))
                .fit().centerCrop().placeholder(R.drawable.placeholder)
                .into(viewHolder.image);
    }

    public void replaceData(List<Gift> gifts) {
        setList(gifts);
        notifyDataSetChanged();
    }

    private void setList(List<Gift> gifts) {
        this.gifts = gifts;
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public Gift getItem(int position) {
        return gifts.get(position);
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
         * Restant au cadeau
         */
        public TextView remainder;

        /**
         * Prix du cadeau
         */
        public TextView price;

        /**
         * Image associée au cadeau
         */
        public ImageView image;

        public RecyclerView detailRecyclerView;
        public Button seeMoreButton;

        /**
         * Listener sur le clic de la personne
         */
        private GiftListActivity.GiftItemListener giftItemListener;


        /**
         * Constructeur
         * @param itemView
         */
        public ViewHolder(final View itemView, GiftListActivity.GiftItemListener listener) {
            super(itemView);
            giftItemListener = listener;

            name = (TextView) itemView.findViewById(R.id.gift_name);
            price = (TextView) itemView.findViewById(R.id.gift_price);
            amount = (TextView) itemView.findViewById(R.id.gift_amount);
            remainder = (TextView) itemView.findViewById(R.id.gift_remainder);
            image = (ImageView) itemView.findViewById(R.id.gift_image);
            seeMoreButton = (Button) itemView.findViewById(R.id.gift_list_see_more_button);

            detailRecyclerView = (RecyclerView) itemView.findViewById(R.id.gift_list_details);
            detailRecyclerView.setHasFixedSize(true);
            detailRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            itemView.findViewById(R.id.gift_list_detail_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.d(TAG, "Clic détecté dans la liste à la position: " + position);

                    Gift gift = getItem(position);
                    if (giftItemListener != null) {
                        giftItemListener.onGiftClick(gift);
                    }
                }
            });

        }
    }
}
