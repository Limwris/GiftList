package com.nichesoftware.giftlist.presenters;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.session.SessionManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Authentication presenter
 */
/* package */ abstract class AuthenticationPresenter<V extends AuthenticationContract.View>
        extends BaseBarePresenter<V>
        implements AuthenticationContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AuthenticationPresenter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Authentication {@link com.nichesoftware.giftlist.repository.datasource.DataSource}
     */
    @SuppressWarnings("WeakerAccess")
    protected AuthDataSource mAuthDataSource;
    /**
     * Authentication subscription
     */
    private Disposable mAuthSubscription;

    /**
     * Default constructor
     *
     * @param view              View to attach
     * @param authDataSource    Authentication data source
     */
    @SuppressWarnings("WeakerAccess")
    public AuthenticationPresenter(@NonNull V view, @NonNull AuthDataSource authDataSource) {
        super(view);
        mAuthDataSource = authDataSource;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Lifecycle
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        if (mAuthSubscription != null && !mAuthSubscription.isDisposed()) {
            mAuthSubscription.dispose();
        }
        mAuthDataSource = null;
    }

    // region Authentication
    @Override
    public void onAuthentication(final String username, final String password) {
        Log.d(TAG, "onAuthentication");

        User user = new User(username, password);

        mAuthSubscription = mAuthDataSource.authenticate(user)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.displayAuthenticationLoader(true))
                .subscribe(connectedUser -> {
                            Log.d(TAG, "authenticate - onNext: " + connectedUser.getUsername());
                            mAttachedView.onAuthenticationSucceeded();
                        },
                        throwable -> {
                            Log.e(TAG, "authenticate - onError", throwable);
                            mAttachedView.displayAuthenticationLoader(false);
                            mAttachedView.onAuthenticationError();
                        },
                        () -> Log.d(TAG, "authenticate - onComplete"));
    }

    @Override
    public void cancelTask() {
        if (mAuthSubscription != null && !mAuthSubscription.isDisposed()) {
            mAuthSubscription.dispose();
        }
    }

    /**
     * Méthode permettant de savoir si un utilisateur est connecté
     * @return true si un utilisateur est connecté, false sinon
     */
    @Override
    public boolean isConnected() {
        return SessionManager.getInstance().isConnected();
    }

    /**
     * Méthode permettant d'effectuer les RG à la déconnexion d'un utilisateur
     */
    @Override
    public void doDisconnect() {
        final User user = SessionManager.getInstance().getConnectedUser();
        mAuthDataSource.disconnect(user);
    }
    // enregion
}
