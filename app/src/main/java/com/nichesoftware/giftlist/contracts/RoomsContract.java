package com.nichesoftware.giftlist.contracts;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.nichesoftware.giftlist.views.rooms.RoomVO;

import java.util.List;

/**
 * Rooms list contract
 */
public interface RoomsContract {
    interface View extends AuthenticationContract.View {
        /**
         * Affiche les salles passées en paramètre
         *
         * @param rooms      List of {@link RoomVO}
         * @param diffResult Difference between olds items and refreshed one, used by {@link com.nichesoftware.giftlist.views.rooms.RoomAdapter}
         */
        void showRooms(List<RoomVO> rooms, DiffUtil.DiffResult diffResult);

        /**
         * Affiche le détail de la salle passée en paramètre (id)
         *
         * @param roomId   Identifiant de la salle à afficher
         */
        void showRoomDetail(@NonNull String roomId);
    }

    interface Presenter extends AuthenticationContract.Presenter {
        /**
         * Action permettant d'ouvrir le détail d'une salle
         *
         * @param roomId   Salle à ouvrir
         */
        void openRoomDetail(String roomId);

        /**
         * Action permettant de charger la liste des salles
         *
         * @param forceUpdate   Flag indiquant si la mise à jour doit être forcée
         */
        void loadRooms(boolean forceUpdate);
    }
}
