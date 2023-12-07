package com.xd11z.myserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.sql.*;
import java.io.Serializable;

public class ConRApplyRecord implements Serializable
{
    public Integer applyId;//申请的id -> 主键
    public Integer auditStatus; //申请的审核状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public String applyTime;//申请的时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public String auditTime; //审核的时间
    public String rejectReason; //驳回的理由

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public String startTime; //会议的开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public String endTime; //会议的结束时间

    public String theme; //会议主题
    public Integer personCount;//会议人数
    public String digest;//摘要
    public String UserID;//关联的用户ID

    public Integer roomID;//会议室id
    public String roomNo;//会议室门牌号
    public Integer roomFloor;//会议室楼层
    public String roomName;//会议室名称
    private Integer deleted;//有没有被删除

    public ConRApplyRecord() {

    }

    // 获取和设置 applyId 的方法
    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    // 获取和设置 auditStatus 的方法
    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public void setUserID(String ID) {
        this.UserID = ID;
    }

    // 获取和设置 applyTime 的方法
    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    // 获取和设置 auditTime 的方法
    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    // 获取和设置 rejectReason 的方法
    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    // 获取和设置 startTime 的方法
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    // 获取和设置 endTime 的方法
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    // 获取和设置 theme 的方法
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    // 获取和设置 personCount 的方法
    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    // 获取和设置 digest 的方法
    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    // 获取和设置 roomID 的方法
    public Integer getroomID() {
        return roomID;
    }

    public void setroomID(Integer roomID) {
        this.roomID = roomID;
    }

    // 获取和设置 roomNo 的方法
    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    // 获取和设置 roomFloor 的方法
    public Integer getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(Integer roomFloor) {
        this.roomFloor = roomFloor;
    }

    // 获取和设置 roomName 的方法
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    // 获取和设置 deleted 的方法
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public ConRApplyRecord(Integer applyId, Integer auditStatus, String applyTime, String auditTime,
                           String rejectReason, String startTime, String endTime, String theme,
                           Integer personCount, String digest, String UserID, Integer roomID,
                           String roomNo, Integer roomFloor, String roomName) {
        this.applyId = applyId;
        this.auditStatus = auditStatus;
        this.applyTime = applyTime;
        this.auditTime = auditTime;
        this.rejectReason = rejectReason;
        this.startTime = startTime;
        this.endTime = endTime;
        this.theme = theme;
        this.personCount = personCount;
        this.digest = digest;
        this.UserID = UserID;
        this.roomID = roomID;
        this.roomNo = roomNo;
        this.roomFloor = roomFloor;
        this.roomName = roomName;
    }

    public ConRApplyRecord(ResultSet resultSet) throws SQLException {
        // 从 ResultSet 中逐一获取数据并初始化对象
        this.applyId = resultSet.getInt("ApplyId");
        this.auditStatus = resultSet.getInt("AuditStatus");
        this.applyTime = resultSet.getString("ApplyTime");
        this.auditTime = resultSet.getString("AuditTime");
        this.rejectReason = resultSet.getString("RejectReason");
        this.startTime = resultSet.getString("StartTime");
        this.endTime = resultSet.getString("EndTime");
        this.theme = resultSet.getString("Theme");
        this.personCount = resultSet.getInt("PersonCount");
        this.digest = resultSet.getString("MeetingDigest");
        this.UserID = resultSet.getString("UserID");
        this.roomID = resultSet.getInt("RoomID");
        this.roomNo = resultSet.getString("RoomNo");
        this.roomFloor = resultSet.getInt("RoomFloor");
        this.roomName = resultSet.getString("RoomName");
        this.deleted = resultSet.getInt("IsDeleted");
    }
}
