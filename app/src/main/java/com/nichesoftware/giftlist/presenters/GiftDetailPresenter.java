package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.GiftDetailContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Gift;

/**
 * Created by n_che on 09/06/2016.
 */
public class GiftDetailPresenter implements GiftDetailContract.UserActionListener {
    private static final String TAG = GiftDetailPresenter.class.getSimpleName();

    /**
     * Data provider
     */
    private DataProvider dataProvider;

    /**
     * View
     */
    private GiftDetailContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public GiftDetailPresenter(@NonNull GiftDetailContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void updateGift(Gift gift, double allocatedAmount) {

    }
}
