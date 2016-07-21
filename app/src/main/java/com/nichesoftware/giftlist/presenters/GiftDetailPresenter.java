package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.support.design.BuildConfig;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.GiftDetailContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Gift;

/**
 * Created by n_che on 09/06/2016.
 */
public class GiftDetailPresenter extends AbstractPresenter implements GiftDetailContract.UserActionListener {
    private static final String TAG = GiftDetailPresenter.class.getSimpleName();

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
    public void updateGift(Gift gift, int roomId, double allocatedAmount) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "updateGift");
        }
        view.showLoader();

        dataProvider.updateGift(roomId, gift.getId(),
                allocatedAmount, new DataProvider.Callback() {
                    @Override
                    public void onSuccess() {
                        view.hideLoader();
                        view.onUpdateGiftSuccess();
                    }

                    @Override
                    public void onError() {
                        view.hideLoader();
                        view.onUpdateGiftFailed();
                    }
                });
    }
}
