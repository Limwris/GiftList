package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.GiftListContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Gift;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class GiftListPresenter implements GiftListContract.UserActionListener {
    private static final String TAG = GiftListPresenter.class.getSimpleName();

    /**
     * Data provider
     */
    private DataProvider dataProvider;

    /**
     * View
     */
    private GiftListContract.View giftListView;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public GiftListPresenter(@NonNull GiftListContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.giftListView = view;
    }

    @Override
    public void loadGifts(final int roomId, boolean forceUpdate) {
        // Show loader
        giftListView.showLoader();

        dataProvider.getGifts(forceUpdate, roomId, new DataProvider.LoadGiftsCallback() {
            @Override
            public void onGiftsLoaded(List<Gift> gifts) {
                // Cache le loader
                giftListView.hideLoader();

                if (gifts != null) {
                    giftListView.showGifts(gifts);
                } else {
                    // Gérer erreurs webservice
                }
            }
        });
    }

    @Override
    public void openGiftDetail(Gift gift) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("Le cadeau [id=%s, nom=%s, amount=%f] a été cliqué...", gift.getId(), gift.getName(), gift.getPrice()));
        }
        giftListView.showGiftDetail(gift);
    }
}
