package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.GiftListContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Gift;

import java.util.List;

/**
 * "Gifts list" presenter
 */
public class GiftListPresenter extends AuthenticationPresenter<GiftListContract.View> implements GiftListContract.Presenter {
    private static final String TAG = GiftListPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public GiftListPresenter(@NonNull GiftListContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }


    @Override
    public void loadGifts(final int roomId, boolean forceUpdate) {
        // Show loader
        mAttachedView.showLoader();

        mDataProvider.getGifts(forceUpdate, roomId, new DataProvider.LoadGiftsCallback() {
            @Override
            public void onGiftsLoaded(List<Gift> gifts) {
                // Cache le loader
                mAttachedView.hideLoader();

                if (gifts != null) {
                    mAttachedView.showGifts(gifts);
                } else {
                    // Gérer erreurs webservice
                }
            }
        });
    }

    @Override
    public void openGiftDetail(Gift gift) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("Le cadeau [id=%s, nom=%s, amount=%f] a été cliqué...",
                    gift.getId(), gift.getName(), gift.getPrice()));
        }
        mAttachedView.showGiftDetail(gift);
    }

    @Override
    public void leaveCurrentRoom(int roomId) {
        mDataProvider.leaveRoom(roomId, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                mAttachedView.onLeaveRoomSuccess();
            }

            @Override
            public void onError() {
                mAttachedView.onLeaveRoomError();
            }
        });
    }

    @Override
    public boolean isInvitationAvailable() {
        return !mDataProvider.isDisconnectedUser();
    }
}
