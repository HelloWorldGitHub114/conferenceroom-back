package com.xd11z.myserver.myserver.entity;

import java.io.Serializable;

/**
 * 会议室实体类
 * 房号、名字、楼层、房间类型（大、小会议室）、人数、面积、状态（是否可用）
 */
public class ConferenceRoom implements Serializable{
    public String roomNo;
    public String roomName;
    public String roomFloor;
    public String roomType;
    public int roomSize;
    public int roomArea;
    public int roomState;

    public ConferenceRoom(String roomNo, String roomName, String roomFloor, String roomType, int roomSize, int roomArea, int roomState) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.roomFloor = roomFloor;
        this.roomType = roomType;
        this.roomSize = roomSize;
        this.roomArea = roomArea;
        this.roomState = roomState;
    }
}