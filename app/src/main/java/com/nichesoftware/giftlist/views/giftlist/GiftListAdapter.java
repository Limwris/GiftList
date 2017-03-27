package com.nichesoftware.giftlist.views.giftlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    private List<GiftVO> gifts = new ArrayList<>();

    /**
     * Listener sur le clic d'un cadeau
     */
    private final GiftListActivity.GiftItemListener giftItemListener;

    /**
     * Constructeur
     *
     * @param giftItemListener      Click listener
     */
    public GiftListAdapter(GiftListActivity.GiftItemListener giftItemListener) {
        this.giftItemListener = giftItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View giftView = inflater.inflate(R.layout.gift_list_item_view, parent, false);

        return new ViewHolder(giftView, giftItemListener, new OnSeeMoreClickListener() {
            @Override
            public void onSeeMoreClick(int position) {
                for (GiftVO gift : gifts) {
                    // If the last expanded postion were not the current one
                    if (gift.isExpanded() && gifts.indexOf(gift) != position) {
                        gift.setExpanded(false);
                    }
                }
                // Expand the given position
                gifts.get(position).setExpanded(!gifts.get(position).isExpanded());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if (position < gifts.size()) {
            GiftVO giftVO = gifts.get(position);
            viewHolder.bindData(giftVO);
        }
    }

    public void replaceData(@NonNull List<GiftVO> gifts) {
        this.gifts = gifts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    /* package */ interface OnSeeMoreClickListener {
        void onSeeMoreClick(final int position);
    }

    /* package */ class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Nom du cadeau
         */
        private TextView name;

        /**
         * Participation du cadeau
         */
        private TextView amount;

        /**
         * Restant au cadeau
         */
        private TextView remainder;

        /**
         * Prix du cadeau
         */
        private TextView price;

        /**
         * Image associée au cadeau
         */
        private ImageView image;

        private RecyclerView detailRecyclerView;
        private AppCompatButton seeMoreButton;

        /**
         * Listener sur le clic de la personne
         */
        private final GiftListActivity.GiftItemListener giftItemListener;

        private final OnSeeMoreClickListener mSeeMoreClickListener;

        /**
         * Constructeur
         * @param itemView
         */
        /* package */ ViewHolder(final View itemView, GiftListActivity.GiftItemListener listener,
                                 OnSeeMoreClickListener seeMoreClickListener) {
            super(itemView);
            giftItemListener = listener;
            mSeeMoreClickListener = seeMoreClickListener;

            name = (TextView) itemView.findViewById(R.id.gift_name);
            price = (TextView) itemView.findViewById(R.id.gift_price);
            amount = (TextView) itemView.findViewById(R.id.gift_amount);
            remainder = (TextView) itemView.findViewById(R.id.gift_remainder);
            image = (ImageView) itemView.findViewById(R.id.gift_image);
            seeMoreButton = (AppCompatButton) itemView.findViewById(R.id.gift_list_see_more_button);

            detailRecyclerView = (RecyclerView) itemView.findViewById(R.id.gift_list_details);
            detailRecyclerView.setHasFixedSize(true);
            detailRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));

            itemView.findViewById(R.id.gift_list_detail_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.d(TAG, "Clic détecté dans la liste à la position: " + position);

                    if (position < gifts.size()) {
                        GiftVO giftVO = gifts.get(position);
                        if (giftItemListener != null) {
                            giftItemListener.onGiftClick(giftVO.getId());
                        }
                    }
                }
            });

        }

        /* package */ void bindData(GiftVO gift) {
            if (!gift.isExpanded()) {
                detailRecyclerView.setVisibility(View.GONE);
            } else {
                detailRecyclerView.setVisibility(View.VISIBLE);
            }

            name.setText(gift.getName());
            price.setText(gift.getPrice(), TextView.BufferType.SPANNABLE);
            amount.setText(gift.getAmount(), TextView.BufferType.SPANNABLE);
            remainder.setText(gift.getRemainder(), TextView.BufferType.SPANNABLE);
            detailRecyclerView.setAdapter(new GiftListDetailAdapter(gift.getDetailVO()));
            seeMoreButton.setOnClickListener(view -> {
                if (mSeeMoreClickListener != null) {
                    mSeeMoreClickListener.onSeeMoreClick(getAdapterPosition());
                }
            });

            Picasso.with(image.getContext())
                    .load(gift.getImageUrl())
                    .fit().centerCrop().placeholder(R.drawable.placeholder)
                    .into(image);
        }
    }
}
