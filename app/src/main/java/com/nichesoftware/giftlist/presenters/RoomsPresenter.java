package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.RoomsContract;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSource;
import com.nichesoftware.giftlist.utils.ResourcesUtils;
import com.nichesoftware.giftlist.views.rooms.RoomVO;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Rooms list presenter
 */
public class RoomsPresenter extends BasePresenter<RoomsContract.View, Room>
        implements RoomsContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = RoomsPresenter.class.getSimpleName();

    // Fields
    private Disposable mLoadRoomsSubscription;

    /**
     * Constructor
     *
     * @param view                  View to attach
     * @param cache                 Cache
     * @param connectedDataSource   The cloud data provider
     * @param authDataSource        Authentication data source
     */
    public RoomsPresenter(@NonNull RoomsContract.View view, @NonNull Cache<Room> cache,
                          @NonNull CloudDataSource<Room> connectedDataSource,
                          @NonNull AuthDataSource authDataSource) {
        super(view, cache, connectedDataSource, authDataSource);
    }

    @Override
    public void openRoomDetail(String roomId) {
        Log.d(TAG, String.format("La salle [id=%s] a été cliquée...", roomId));
        mAttachedView.showRoomDetail(roomId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadRoomsSubscription != null && !mLoadRoomsSubscription.isDisposed()) {
            mLoadRoomsSubscription.dispose();
        }
    }

    @Override
    public void loadRooms(boolean forceUpdate) {
        Log.d(TAG, "loadRooms");

        mLoadRoomsSubscription = Observable.defer(() -> {
            if (forceUpdate) {
                mCache.evictAll();
            }
            return mDataProvider.getAll();
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .map(this::map)
                .subscribe(rooms -> mAttachedView.showRooms(rooms),
                        throwable -> {
                            Log.e(TAG, "loadRooms", throwable);
                            mAttachedView.showError(throwable.getMessage());
                        });
    }

    /**
     * Mapper from {@link List} of {@link Room} to {@link List} of {@link RoomVO}
     * @param rooms     {@link List} of {@link Room}
     * @return          Corresponding {@link List} of {@link RoomVO}
     */
    private List<RoomVO> map(List<Room> rooms) {
        List<RoomVO> roomVOs = new ArrayList<>();
        MessageFormat messageFormat = new MessageFormat(ResourcesUtils.getString(R.string.gift_description));
        for (Room room : rooms) {
            Object[] messageArgs = { room.getBoughtGiftListSize(), room.getGiftListSize() };
            RoomVO vo = new RoomVO(room.getId(), room.getRoomName(), messageFormat.format(messageArgs));
            roomVOs.add(vo);
        }
        return roomVOs;
    }
}
