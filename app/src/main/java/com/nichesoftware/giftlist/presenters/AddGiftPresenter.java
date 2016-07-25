package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.AddGiftContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 09/06/2016.
 */
public class AddGiftPresenter extends AbstractPresenter implements AddGiftContract.UserActionListener {
    private static final String TAG = AddGiftPresenter.class.getSimpleName();

    /**
     * View
     */
    private AddGiftContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public AddGiftPresenter(@NonNull AddGiftContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void addGift(int roomId, String name, double price, double amount,
                        final String filePath) {
        dataProvider.addGift(roomId, name, price, amount, filePath,
                new DataProvider.Callback() {
                    @Override
                    public void onSuccess() {
                        view.onCreateGiftSuccess();
                    }

                    @Override
                    public void onError() {
                        view.onCreateGiftFailed();
                    }
                });
    }
}
