package com.nichesoftware.giftlist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Invitation to a room
 */
public class Invitation implements Parcelable {
    /**
     * Utilisateur étant invité
     */
    private User invitedUser;
    /**
     * Salle dans laquelle l'utilisateur est invitée
     */
    private Room room;
    /**
     * Utilisateur invitant
     */
    private User invitingUser;
    /**
     * Token d'invitation
     */
    private String token;

    /**
     * Default constructor
     */
    public Invitation() {
    }

    /**
     * Getter sur l'utilisateur étant invité
     * @return
     */
    public User getInvitedUser() {
        return invitedUser;
    }

    /**
     * Setter sur l'utilisateur étant invité
     * @param invitedUser
     */
    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    /**
     * Getter sur la salle dans laquelle l'utilisateur est invitée
     * @return
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Setter sur la salle dans laquelle l'utilisateur est invitée
     * @param room
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Getter sur l'utilisateur invitant
     * @return
     */
    public User getInvitingUser() {
        return invitingUser;
    }

    /**
     * Setter sur l'utilisateur invitant
     * @param invitingUser
     */
    public void setInvitingUser(User invitingUser) {
        this.invitingUser = invitingUser;
    }

    /**
     * Getter sur le token d'invitation
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter sur le token d'invitation
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    // region Parcelable
    protected Invitation(Parcel in) {
        invitedUser = in.readParcelable(User.class.getClassLoader());
        room = in.readParcelable(Room.class.getClassLoader());
        invitingUser = in.readParcelable(User.class.getClassLoader());
        token = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(invitedUser, flags);
        dest.writeParcelable(room, flags);
        dest.writeParcelable(invitingUser, flags);
        dest.writeString(token);
    }

    public static final Creator<Invitation> CREATOR = new Creator<Invitation>() {
        @Override
        public Invitation createFromParcel(Parcel in) {
            return new Invitation(in);
        }

        @Override
        public Invitation[] newArray(int size) {
            return new Invitation[size];
        }
    };
    // endregion
}
