package com.nichesoftware.giftlist.contracts;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.model.Room;

import java.util.List;

/**
 * Rooms list contract
 */
public interface RoomsContract {
    interface View extends AuthenticationContract.View {
        /**
         * Affiche les salles passées en paramètre
         * @param rooms
         */
        void showRooms(List<Room> rooms);
        /**
         * Affiche le détail de la salle passée en paramètre (id)
         * @param roomId   Identifiant de la salle à afficher
         */
        void showRoomDetail(@NonNull int roomId);
    }
    interface Presenter extends AuthenticationContract.Presenter {
        /**
         * Action permettant d'ouvrir le détail d'une salle
         * @param room   Salle à ouvrir
         */
        void openRoomDetail(Room room);
        /**
         * Action permettant de charger la liste des salles
         * @param forceUpdate   Flag indiquant si la mise à jour doit être forcée
         */
        void loadRooms(boolean forceUpdate);
    }
}
