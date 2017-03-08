package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.support.design.BuildConfig;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.GiftDetailContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Gift;

/**
 * Gift detail presenter
 */
public class GiftDetailPresenter extends AuthenticationPresenter<GiftDetailContract.View> implements GiftDetailContract.Presenter {
    private static final String TAG = GiftDetailPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public GiftDetailPresenter(@NonNull GiftDetailContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }

    @Override
    public void updateGift(Gift gift, int roomId, double allocatedAmount,
                           final String description, final String filePath) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "updateGift");
        }
        mAttachedView.showLoader();

        mDataProvider.updateGift(roomId, gift.getId(), allocatedAmount, description, filePath,
                new DataProvider.Callback() {
                    @Override
                    public void onSuccess() {
                        mAttachedView.hideLoader();
                        mAttachedView.onUpdateGiftSuccess();
                    }

                    @Override
                    public void onError() {
                        mAttachedView.hideLoader();
                        mAttachedView.onUpdateGiftFailed();
                    }
                });
    }

    /**
     * Méthode retournant l'utilisateur courant
     * @return
     */
    @Override
    public String getCurrentUsername() {
        return mDataProvider.getCurrentUser();
    }
}
