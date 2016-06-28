package com.nichesoftware.giftlist.presenters;

import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by Kattleya on 28/06/2016.
 */
public class AbstractPresenter {
    /**
     * Data provider
     */
    protected DataProvider dataProvider;

    public void doDisconnect() {
        dataProvider.doDisconnect();
    }
}
