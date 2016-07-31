package com.nichesoftware.giftlist.presenters;

import com.nichesoftware.giftlist.contracts.AbstractContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by Kattleya on 28/06/2016.
 */
public class AbstractPresenter implements AbstractContract.UserActionListener {
    /**
     * Data provider
     */
    protected DataProvider dataProvider;

    /**
     * Méthode permettant de savoir si un utilisateur est connecté
     * @return true si un utilisateur est connecté, false sinon
     */
    @Override
    public boolean isConnected() {
        return !dataProvider.isDisconnectedUser();
    }

    /**
     * Méthode permettant d'effectuer les RG à la déconnexion d'un utilisateur
     */
    @Override
    public void doDisconnect() {
        dataProvider.doDisconnect();
    }

    /**
     * Méthode retournant l'utilisateur courant
     * @return
     */
    @Override
    public String getCurrentUsername() {
        return dataProvider.getCurrentUser();
    }
}
