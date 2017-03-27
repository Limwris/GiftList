package com.nichesoftware.giftlist.views.rooms;

import com.nichesoftware.giftlist.model.Room;

/**
 * View Object for the {@link RoomAdapter}
 */
public class RoomVO {
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
}
