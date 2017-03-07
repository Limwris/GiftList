package com.nichesoftware.giftlist.views.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.SplashScreenContract;
import com.nichesoftware.giftlist.presenters.SplashScreenPresenter;
import com.nichesoftware.giftlist.views.rooms.RoomsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Splash screen
 */
public class SplashScreenActivity extends AppCompatActivity implements SplashScreenContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static final int STARTUP_DELAY = 300;
    private static final int ANIM_ITEM_DURATION = 1000;

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Listener sur les actions de l'utilisateur
     */
    private SplashScreenContract.UserActionListener actionsListener;

    /**
     * Unbinder Butter Knife
     */
    private Unbinder mButterKnifeUnbinder;

    /**
     * Graphical components
     */
    @BindView(R.id.login_logo)
    ImageView mLogoImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        setTheme(R.style.AppTheme); // Supprime le windowBackground
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mButterKnifeUnbinder = ButterKnife.bind(this);

        actionsListener = new SplashScreenPresenter(this,
                Injection.getDataProvider(this));

        /**
         * Animations
         */
        ViewCompat.animate(mLogoImageView)
                .translationY(-getContext().getResources().getInteger(R.integer.login_logo_translation))
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                actionsListener.doRGSplashScreen();
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mButterKnifeUnbinder.unbind();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Implement methods                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void doShowDisconnectedActivity() {
        Log.d(TAG, "doShowDisconnectedActivity");

        final Intent intent = new Intent(this, LaunchScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void doShowConnectedActivity() {
        Log.d(TAG, "doShowConnectedActivity");

        final Intent intent = new Intent(this, RoomsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoader() {
        // Do nothing
    }

    @Override
    public void hideLoader() {
        // Do nothing
    }

    @Override
    public Context getContext() {
        return this;
    }
}
