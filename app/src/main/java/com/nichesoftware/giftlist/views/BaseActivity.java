package com.nichesoftware.giftlist.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nichesoftware.giftlist.presenters.IPresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Common abstract activity.
 */
public abstract class BaseActivity<P extends IPresenter>
        extends AppCompatActivity
        implements IView, INavigationListener {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = BaseActivity.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Instance of the presenter bound to this activity
     */
    protected P presenter;

    /**
     * Unbinder ButterKnife to handle the Activity lifecycle
     */
    protected Unbinder mButterKnifeUnbinder;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Lifecycle
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(getContentView());
        // Initialize presenter
        initPresenter();
        // Bind ButterKnife
        mButterKnifeUnbinder = ButterKnife.bind(this);
        // Initialize view
        initView();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (presenter != null) {
            presenter.onDestroy();
        }
        // Unbind ButterKnife
        mButterKnifeUnbinder.unbind();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Abstract methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retrieve the layout of the activity
     *
     * @return layout
     */
    protected abstract @LayoutRes int getContentView();

    /**
     * Create an instance of the presenter associated to this activity
     *
     * @return presenter
     */
    protected abstract P newPresenter();

//    protected void onFragmentChanged(@NonNull BaseFragment fragment, final boolean isAdded) {
//        // Update toolbar
//        final String title = fragment.getTitle();
//        if (hasToolbar()) {
//            if(!StringUtils.isEmpty(title)) {
//                updateToolbar(title);
//            }
//            if(isAdded) {
//                updateToolbarMenu(true);
//            }
//        }
//    }

    /**********************************************************************************************/
    /***                             Initialization methods                                     ***/
    /**********************************************************************************************/

    /**
     * Initialize the objects of the view (e.g., onClick...)
     */
    @CallSuper
    protected void initView() {
        // Nothing by default
    }

    /**
     * Initialize the presenter
     */
    protected void initPresenter() {
        Log.d(TAG, "initPresenter");
        presenter = newPresenter();
        if (presenter != null) {
            presenter.onCreate();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Getters & setters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter on the presenter
     * <p>
     * If the presenter has been already created, return this instance,
     * otherwise create it with the newPresenter() method.
     *
     * @return presenter
     */
    @NonNull
    public P getPresenter() {
        if (presenter == null) {
            return newPresenter();
        } else {
            return presenter;
        }
    }

    /**
     * Setter on the presenter
     *
     * @param presenter
     */
    public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }

//    protected BaseFragment getCurrentFragment() {
//        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame_identifier);
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///   Navigation
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void performChangeActivity(@NonNull final Class<? extends BaseActivity> activity,
                                      @Nullable Bundle bundle) {
        Log.d(TAG, "performChangeActivity");
        Intent intent = new Intent(this, activity);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Fragment handler
    ////////////////////////////////////////////////////////////////////////////////////////////////

//    @Override
//    public void addBackStackFragment(@NonNull final String fragmentTag, @Nullable Bundle bundle) {
//        // Retrieve the fragment manager
//        FragmentManager manager = getSupportFragmentManager();
//        // Create a new fragment transaction
//        FragmentTransaction transaction = manager.beginTransaction();
//        // Gets the current fragment
//        Fragment fragment = manager.findFragmentByTag(fragmentTag);
//
//        if(bundle == null) {
//            bundle = new Bundle();
//        }
//
//        if (fragment == null) {
//            // Instanciate fragment
//            fragment = Fragment.instantiate(this, fragmentTag, bundle);
//
//            // Replace whatever is in the fragment_container view with this fragment,
//            // and add the transaction to the back stack
//            transaction.addToBackStack(fragmentTag);
//            transaction.add(R.id.main_frame_identifier, fragment, fragmentTag);
//
//            // Commit the transaction
//            transaction.commit();
//        } else { // Already in the stack
//            // Cannot setArgument on an active fragment {@link Fragment#setArgument Fragment.setArgument(Bundle)}
//            // When using this, dev should be careful to have put an original bundle
//            final Bundle arguments = fragment.getArguments();
//            if(arguments != null) {
//                arguments.clear();
//                arguments.putAll(bundle);
//            } else {
//                fragment.setArguments(bundle);
//            }
//
//            transaction.show(fragment);
//            // Indicates the fragment will appear
//            ((BaseFragment)fragment).willAppear();
//        }
//
//        // Indicates fragment changed
//        onFragmentChanged((BaseFragment) fragment, true);
//    }
//
//    @Override
//    public void replaceCurrentFragment(@NonNull final String fragmentTag, @Nullable Bundle bundle) {
//        // Gets the current fragment
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
//
//        if(bundle == null) {
//            bundle = new Bundle();
//        }
//
//        if (fragment == null) {
//            // Instanciate fragment
//            fragment = Fragment.instantiate(this, fragmentTag, bundle);
//        } else {
//            // Cannot setArgument on an active fragment {@link Fragment#setArgument Fragment.setArgument(Bundle)}
//            // When using this, dev should be careful to have put an original bundle
//            final Bundle arguments = fragment.getArguments();
//            if(arguments != null) {
//                arguments.clear();
//                arguments.putAll(bundle);
//            } else {
//                fragment.setArguments(bundle);
//            }
//            // Indicates the fragment will appear
//            ((BaseFragment)fragment).willAppear();
//        }
//
//        //replace fragment
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_frame_identifier, fragment, fragmentTag)
//                .commit();
//
//        // Indicates fragment changed
//        onFragmentChanged((BaseFragment) fragment, false);
//    }
}
