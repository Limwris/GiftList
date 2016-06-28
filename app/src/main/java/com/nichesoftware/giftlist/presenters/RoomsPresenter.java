package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.RoomsContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Room;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class RoomsPresenter extends AbstractPresenter implements RoomsContract.UserActionListener {
    private static final String TAG = RoomsPresenter.class.getSimpleName();

    /**
     * View
     */
    private RoomsContract.View roomsView;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public RoomsPresenter(@NonNull RoomsContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.roomsView = view;
    }

    @Override
    public void openRoomDetail(Room room) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("La salle [id=%s, nom=%s, occasion=%s] a été cliquée...", room.getId(), room.getRoomName(), room.getOccasion()));
        }
        roomsView.showRoomDetail(room.getId());
    }

    @Override
    public void loadRooms(boolean forceUpdate) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "loadRooms");
        }

        // Show loader
        roomsView.showLoader(null);

//        if (forceUpdate) {
//            dataProvider.refreshData();
//        }

        dataProvider.getRooms(forceUpdate, new DataProvider.LoadRoomsCallback() {
            @Override
            public void onRoomsLoaded(List<Room> rooms) {
                // Cache le loader
                roomsView.hideLoader();

                if (rooms != null) {
                    roomsView.showRooms(rooms);
                } else {
                    // Gérer erreurs webservice
                }
            }
        });

    }
}
