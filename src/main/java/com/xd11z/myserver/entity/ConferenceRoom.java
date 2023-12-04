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
}