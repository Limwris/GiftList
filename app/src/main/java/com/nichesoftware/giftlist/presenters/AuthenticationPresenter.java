package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.dataproviders.events.AuthenticationFailedEvent;
import com.nichesoftware.giftlist.dataproviders.events.AuthenticationSucceededEvent;
import com.nichesoftware.giftlist.utils.BusProvider;
import com.squareup.otto.Subscribe;

/**
 * Authentication presenter
 */
public abstract class AuthenticationPresenter<V extends AuthenticationContract.View> extends BasePresenter<V> implements AuthenticationContract.Presenter {
    private static final String TAG = AuthenticationPresenter.class.getSimpleName();

    /**
     * Bus listener for authentication (composition)
     */
    protected AuthenticationBusListener authenticationEventListener;

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public AuthenticationPresenter(@NonNull V view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }

    /**********************************************************************************************/
    /***                                   Lifecycle                                            ***/
    /**********************************************************************************************/

    @Override
    public void onCreate() {
        Log.d(TAG, "[AuthenticationPresenter] onCreate");
        super.onCreate();

        authenticationEventListener = new AuthenticationBusListener() {
            @Subscribe
            public void onAuthenticationFailedEventReceived(AuthenticationFailedEvent event) {
                Log.d(TAG, "onAuthenticationFailedEventReceived");
                if (mAttachedView != null) {
                    mAttachedView.hideLoader();
                    mAttachedView.onAuthenticationError();
                }
            }

            @Subscribe
            public void onAuthenticationSucceedEventReceived(AuthenticationSucceededEvent event) {
                Log.d(TAG, "onAuthenticationSucceedEventReceived");
                if (mAttachedView != null) {
                    mAttachedView.hideLoader();
                    mAttachedView.onAuthenticationSucceeded();
                }
            }
        };
        BusProvider.register(authenticationEventListener);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onCreate");
        super.onDestroy();
        BusProvider.unregister(authenticationEventListener);
    }

    /**********************************************************************************************/
    /***                                Authentication                                          ***/
    /**********************************************************************************************/

    @Override
    public void onAuthentication(final String username, final String password) {
        Log.d(TAG, "onAuthentication");
        mAttachedView.showLoader();
        mDataProvider.logIn(username, password);
    }

    /**
     * Bus event listener for authentication process (composition)
     */
    interface AuthenticationBusListener { }



    /**
     * Méthode permettant de savoir si un utilisateur est connecté
     * @return true si un utilisateur est connecté, false sinon
     */
    @Override
    public boolean isConnected() {
        return !mDataProvider.isDisconnectedUser();
    }

    /**
     * Méthode permettant d'effectuer les RG à la déconnexion d'un utilisateur
     */
    @Override
    public void doDisconnect() {
        mDataProvider.doDisconnect();
    }
}
//public class AuthenticationPresenter extends AbstractPresenter implements AuthenticationContract.UserActionListener {
//    private static final String TAG = AuthenticationPresenter.class.getSimpleName();
//
//    /**
//     * View
//     */
//    private AuthenticationContract.View view;
//
//    /**
//     * Constructeur
//     * @param view
//     * @param dataProvider
//     */
//    public AuthenticationPresenter(@NonNull AuthenticationContract.View view,
//                                   @NonNull DataProvider dataProvider) {
//        this.dataProvider = dataProvider;
//        this.view = view;
//    }
//
//    @Override
//    public void authenticate(final String username, final String password, final AuthenticationContract.OnAuthenticationCallback callback) {
//
//        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
//            view.showLoader();
//
//            dataProvider.logIn(username, password, new DataProvider.Callback() {
//                @Override
//                public void onSuccess() {
//                    if (BuildConfig.DEBUG) {
//                        Log.d(TAG, "authenticate - connected - onSuccess");
//                    }
//                    view.hideLoader();
//                    if (callback != null) {
//                        callback.onSuccess();
//                    }
//                    view.dismiss();
//                }
//
//                @Override
//                public void onError() {
//                    if (BuildConfig.DEBUG) {
//                        Log.d(TAG, "authenticate - connected - onError");
//                    }
//                    view.hideLoader();
//                    if (callback != null) {
//                        callback.onError();
//                    }
//                    view.dismiss();
//                }
//            });
//        } else {
//            if (callback != null) {
//                callback.onError();
//            }
//        }
//    }
//}
