package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.AddRoomContract;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.ConnectedDataSource;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Add room presenter
 */
public class AddRoomPresenter extends BasePresenter<AddRoomContract.View, Room>
        implements AddRoomContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddRoomPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view                  View to attach
     * @param cache                 Cache
     * @param connectedDataSource   The cloud data provider
     * @param authDataSource        Authentication data source
     */
    public AddRoomPresenter(@NonNull AddRoomContract.View view, @NonNull Cache<Room> cache,
                            @NonNull ConnectedDataSource<Room> connectedDataSource,
                            @NonNull AuthDataSource authDataSource) {
        super(view, cache, connectedDataSource, authDataSource);
    }

    @Override
    public void addRoom(final String roomName, final String occasion) {
        Room room = new Room(roomName, occasion);
        mDataProvider.add(room)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(addGift -> mAttachedView.onCreateRoomSuccess(),
                        throwable -> {
                            mAttachedView.showError(throwable.getMessage());
                            mAttachedView.onCreateRoomFailed();
                        },
                        () -> Log.d(TAG, "addRoom: onComplete"));
    }
}
