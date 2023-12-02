package com.xd11z.myserver.data;

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
    public int roomState;

    public ConferenceRoom(String roomNo, String roomType, String roomFloor, int roomSize, int roomState) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.roomFloor = roomFloor;
        this.roomSize = roomSize;
        this.roomType = roomType;
        this.roomState = roomState;
    }
}