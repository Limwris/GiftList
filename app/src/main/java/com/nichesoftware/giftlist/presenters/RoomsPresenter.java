package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.RoomsContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Room;

import java.util.List;

/**
 * Rooms list presenter
 */
public class RoomsPresenter extends AbstractPresenter<RoomsContract.View>
        implements RoomsContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = RoomsPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public RoomsPresenter(@NonNull RoomsContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }

    @Override
    public void openRoomDetail(Room room) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("La salle [id=%s, nom=%s, occasion=%s] a été cliquée...", room.getId(), room.getRoomName(), room.getOccasion()));
        }
        mAttachedView.showRoomDetail(room.getId());
    }

    @Override
    public void loadRooms(boolean forceUpdate) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "loadRooms");
        }

        // Show loader
        mAttachedView.showLoader();

        mDataProvider.getRooms(forceUpdate, new DataProvider.LoadRoomsCallback() {
            @Override
            public void onRoomsLoaded(List<Room> rooms) {
                // Cache le loader
                mAttachedView.hideLoader();

                if (rooms != null) {
                    mAttachedView.showRooms(rooms);
                } else {
                    // Gérer erreurs webservice
                }
            }
        });

    }
}
