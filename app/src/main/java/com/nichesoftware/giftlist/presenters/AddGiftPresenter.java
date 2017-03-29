package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.AddGiftContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSource;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Add gift presenter
 */
public class AddGiftPresenter extends BasePresenter<AddGiftContract.View, Gift>
        implements AddGiftContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddGiftPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view                  View to attach
     * @param cache                 Cache
     * @param connectedDataSource   The cloud data provider
     * @param authDataSource        Authentication data source
     */
    public AddGiftPresenter(@NonNull AddGiftContract.View view, @NonNull Cache<Gift> cache,
                            @NonNull CloudDataSource<Gift> connectedDataSource,
                            @NonNull AuthDataSource authDataSource) {
        super(view, cache, connectedDataSource, authDataSource);
    }

    @Override
    public void addGift(String roomId, String name, double price, double amount,
                        final String description, final String filePath) {
        Gift gift = new Gift(name, price, amount, description, filePath);
        mDataProvider.add(gift)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(addGift -> mAttachedView.onCreateGiftSuccess(),
                        throwable -> {
                            mAttachedView.showError(throwable.getMessage());
                            mAttachedView.onCreateGiftFailed();
                        },
                        () -> Log.d(TAG, "addGift: onComplete"));
    }
}
