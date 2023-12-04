package com.xd11z.myserver.entity;

import com.xd11z.myserver.entity.ConferenceRoom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 装置
 * 装置id，名称，设备数量，会议室id
 */
public class Device implements Serializable {

    public static final long serialVersionUID = 1L;

    public Integer did;

    @JsonProperty("dname")  // 指定 JSON 字段名称为 "dName"
    public String dname;

    public Integer dnumber;

    public Integer roomID;

    public Integer getId() {
        return did;
    }

    public void setId(Integer did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public Integer getRoomId() {
        return roomID;
    }

    public void setRoomId(Integer roomID) {
        this.roomID = roomID;
    }

    public Integer getDnumber() {
        return dnumber;
    }

    public void setDnumber(Integer dnumber) {
        this.dnumber = dnumber;
    }

}