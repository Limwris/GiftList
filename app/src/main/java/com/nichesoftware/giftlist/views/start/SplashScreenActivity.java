package com.nichesoftware.giftlist.views.start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.SplashScreenContract;
import com.nichesoftware.giftlist.presenters.SplashScreenPresenter;
import com.nichesoftware.giftlist.views.BaseActivity;
import com.nichesoftware.giftlist.views.rooms.RoomsActivity;

import butterknife.BindView;

/**
 * Splash screen
 */
public class SplashScreenActivity extends BaseActivity<SplashScreenContract.Presenter>
        implements SplashScreenContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static final int STARTUP_DELAY = 300;
    private static final int ANIM_ITEM_DURATION = 1000;

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Graphical components
     */
    @BindView(R.id.login_logo)
    ImageView mLogoImageView;

    @Override
    protected void initView() {
        super.initView();
        Log.d(TAG, "initView");
        setTheme(R.style.AppTheme); // Supprime le windowBackground

        /**
         * Animations
         */
        ViewCompat.animate(mLogoImageView)
                .translationY(-getResources().getInteger(R.integer.login_logo_translation))
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                presenter.doRGSplashScreen();
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();
    }

    @Override
    protected int getContentView() {
        return R.layout.splash_activity;
    }

    @Override
    protected SplashScreenContract.Presenter newPresenter() {
        return new SplashScreenPresenter(this);
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
    public void showError(@NonNull String message) {

    }
}
