package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.GiftDetailContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.ConnectedDataSource;
import com.nichesoftware.giftlist.utils.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Gift detail presenter
 */
public class GiftDetailPresenter extends BasePresenter<GiftDetailContract.View, Gift>
        implements GiftDetailContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftDetailPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view                  View to attach
     * @param cache                 Cache
     * @param connectedDataSource   The cloud data provider
     * @param authDataSource        Authentication data source
     */
    public GiftDetailPresenter(@NonNull GiftDetailContract.View view, @NonNull Cache<Gift> cache,
                            @NonNull ConnectedDataSource<Gift> connectedDataSource,
                            @NonNull AuthDataSource authDataSource) {
        super(view, cache, connectedDataSource, authDataSource);
    }

    @Override
    public void updateGift(Gift gift, String roomId, double allocatedAmount,
                           final String description, final String filePath) {
        Log.d(TAG, "updateGift");

        Gift updatedGift = gift.clone();
        updatedGift.setAmount(allocatedAmount);
        updatedGift.setDescription(description);
        if (!StringUtils.isEmpty(filePath)) {
            updatedGift.setImageUrl(filePath);
            updatedGift.setImageFileLocal(true);
        }
        mDataProvider.update(updatedGift)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(addGift -> mAttachedView.onUpdateGiftSuccess(),
                        throwable -> mAttachedView.showError(throwable.getMessage()),
                        () -> Log.d(TAG, "addGift: onComplete"));
    }
}
