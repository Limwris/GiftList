package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.contracts.LaunchScreenContract;
import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.repository.cache.UserCache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.provider.AuthDataSourceProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Launch screen presenter
 */
public class LaunchScreenPresenter extends AuthenticationPresenter<LaunchScreenContract.View>
        implements LaunchScreenContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = LaunchScreenPresenter.class.getSimpleName();

    /**
     * Default constructor
     *
     * @param view           View to attach
     * @param authDataSource Authentication data source
     */
    public LaunchScreenPresenter(@NonNull LaunchScreenContract.View view,
                                 @NonNull AuthDataSource authDataSource) {
        super(view, authDataSource);
    }

    // region Splashscreen contract
    @Override
    public void startApplication() {
        Log.d(TAG, "startApplication");
        mAttachedView.showRoomsActivity();
    }

    @Override
    public void register(final String username, final String password) {
        Log.d(TAG, "register");

        User user = new User(username, password);
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        AuthDataSource authProvider = new AuthDataSourceProvider(new UserCache(databaseManager),
                Injection.getService());

        authProvider.register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(connectedUser -> Log.d(TAG, "register - onNext: " + connectedUser.getName()),
                        throwable -> Log.e(TAG, "register - onError", throwable),
                        () -> {
                            Log.d(TAG, "register - onComplete");
                            mAttachedView.showRoomsActivity();
                        });
    }
    // endregion
}
