package com.xd11z.myserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

public class ConRApplyRecord
{
    private Integer applyId;//申请的id -> 主键
    private Integer auditStatus; //申请的审核状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String applyTime;//申请的时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String auditTime; //审核的时间
    private String rejectReason; //驳回的理由

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String startTime; //会议的开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String endTime; //会议的结束时间

    private String theme; //会议主题
    private Integer personCount;//会议人数
    private String digest;//摘要

    private Integer roomId;//会议室id
    private String roomNo;//会议室门牌号
    private String roomFloor;//会议室楼层
    private String roomName;//会议室名称

}
