package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.AddGiftContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Add gift presenter
 */
public class AddGiftPresenter extends AbstractPresenter<AddGiftContract.View>
        implements AddGiftContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddGiftPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public AddGiftPresenter(@NonNull AddGiftContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }

    @Override
    public void addGift(int roomId, String name, double price, double amount,
                        final String description, final String filePath) {
        mDataProvider.addGift(roomId, name, price, amount, description, filePath,
                new DataProvider.Callback() {
                    @Override
                    public void onSuccess() {
                        mAttachedView.onCreateGiftSuccess();
                    }

                    @Override
                    public void onError() {
                        mAttachedView.onCreateGiftFailed();
                    }
                });
    }
}
