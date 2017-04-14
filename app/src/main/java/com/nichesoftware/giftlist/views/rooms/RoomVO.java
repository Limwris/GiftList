package com.nichesoftware.giftlist.views.rooms;

import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.views.adapter.ViewHolderVO;

/**
 * View Object for the {@link RoomAdapter}
 */
public class RoomVO extends ViewHolderVO {
    /**
     * Identifiant unique de la salle
     */
    private String id;
    /**
     * Nom de la salle
     */
    private String roomName;
    /**
     * Description of the room
     */
    private String description;

    /**
     * Default constructor
     * @param id            Id of the bound {@link Room}
     * @param roomName      Name of the bound {@link Room}
     * @param description   Description "
     */
    public RoomVO(String id, String roomName, String description) {
        this.id = id;
        this.roomName = roomName;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomVO vo = (RoomVO) o;

        if (id != null ? !id.equals(vo.id) : vo.id != null) return false;
        if (roomName != null ? !roomName.equals(vo.roomName) : vo.roomName != null) return false;
        return description != null ? description.equals(vo.description) : vo.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (roomName != null ? roomName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
