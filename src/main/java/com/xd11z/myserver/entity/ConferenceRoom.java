package com.xd11z.myserver.entity;

import java.io.Serializable;

/**
 * 会议室实体类
 * 房号、名字、楼层、房间类型（大、小会议室）、人数、面积、状态（是否可用）
 */
public class ConferenceRoom implements Serializable{
    public int roomID;
    public String roomNo;
    public String roomName;
    public int roomFloor;
    public int roomSize;
    public float roomArea;
    public int roomState;

    public ConferenceRoom(int roomID, String roomNo, String roomName, int roomFloor, int roomSize, float roomArea, int roomState)
    {
        this.roomID = roomID;
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.roomFloor = roomFloor;
        this.roomSize = roomSize;
        this.roomArea = roomArea;
        this.roomState = roomState;
    }

    public void setUseable()
    {
        this.roomState=1;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(int roomFloor) {
        this.roomFloor = roomFloor;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public float getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(float roomArea) {
        this.roomArea = roomArea;
    }

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }
}