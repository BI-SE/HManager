package com.wjq.model;

/**
 * Created by deior on 2018/5/20.
 */
public class OrderResult extends RoomOrder{

    private String roomName;

    private String name;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
