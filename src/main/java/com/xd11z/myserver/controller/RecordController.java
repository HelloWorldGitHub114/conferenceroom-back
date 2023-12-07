package com.xd11z.myserver.controller;

import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.annotation.UserLoginToken;
import com.xd11z.myserver.entity.*;
import com.xd11z.myserver.util.*;
import org.springframework.web.bind.annotation.*;
import java.time.ZoneOffset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/record/")
public class RecordController {

    @GetMapping("listbyconditions/{auditState}/{currentPage}/{deleted}")
    public ServerResponse listByConditions(@PathVariable("auditState") Integer auditState,
                                           @PathVariable("currentPage") Integer currentPage,
                                           @PathVariable("deleted") Integer deleted) {
        try {
            List<ConRApplyRecord> records;
            if(deleted==2){
                records = RecordJDBC.listAllRecordsByAuditStatus(auditState, currentPage);
            }
            else{
                records = RecordJDBC.listByConditions(auditState, currentPage, deleted);
            }
            return ServerResponse.success(records);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ServerResponse.fail("查询失败！");
        }
    }

    @GetMapping("gettotal/{auditState}/{deleted}")
    public ServerResponse gettotalbyadmin(@PathVariable("auditState") Integer auditState,
                                           @PathVariable("deleted") Integer deleted) {
        try {
            int num = RecordJDBC.getTotalnumber(auditState, deleted);
            return ServerResponse.success(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ServerResponse.fail("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/gettotalbyuser/{userID}/{auditState}")
    public ServerResponse gettotalbyuser(@PathVariable("userID") String userID,
                                         @PathVariable("auditState") Integer auditState) {
        try {
            int num = RecordJDBC.getTotalnumberbyuser(userID, auditState);
            return ServerResponse.success(num);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.fail("查询失败！");
        }
    }

    @GetMapping("getbyuser/{userID}/{auditState}/{currentPage}")
    public ServerResponse getbyuser(@PathVariable("userID") String userID,
                                    @PathVariable("auditState") Integer auditState,
                                    @PathVariable("currentPage") String currentPageStr) {
        try {
            // 将currentPageStr转换为整数
            Integer currentPage = Integer.parseInt(currentPageStr);

            List<ConRApplyRecord> records = RecordJDBC.listByConditionsForUsers(userID, auditState, currentPage);
            return ServerResponse.success(records);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ServerResponse.fail("查询失败！参数错误：currentPage应为整数。");
        }
    }


    @PutMapping("/changeauditstate")
    public ServerResponse changeAuditState(@RequestBody Map<String, Object> map) throws ParseException {
        if ((Integer) map.get("auditState") == 1) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse((String) map.get("startTime"));
            if (System.currentTimeMillis() > date.getTime()) {
                return ServerResponse.fail("无法通过,开始时间已过,请驳回");
            }

            // 去掉毫秒部分
            String startTimeString = ((String) map.get("startTime")).split("\\.")[0];
            String endTimeString = ((String) map.get("endTime")).split("\\.")[0];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
            LocalDateTime startTime = LocalDateTime.parse(startTimeString, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeString, formatter);

            List<Integer> list = RecordJDBC.searchTimeConflict((Integer) map.get("roomID"), startTime, endTime);
            if (list.size() > 0) {
                return ServerResponse.fail("无法通过,时间已冲突,请驳回请求并说明理由");
            } else {
                RecordJDBC.updateAuditStatusAndTime((Integer) map.get("applyId"), (Integer) map.get("auditState"));

                Map<String, Object> map1 = new HashMap<>();
                map1.put("theme", map.get("theme"));
                map1.put("roomFloor", map.get("roomFloor"));
                map1.put("roomNo", map.get("roomNo"));
                map1.put("startTime", startTime.format(formatter)); // 格式化为字符串
                map1.put("endTime", endTime.format(formatter)); // 格式化为字符串

                return ServerResponse.success("");
            }
        } else {
            // 更新驳回状态
            RecordJDBC.updateAuditStatusRejection(
                    (Integer) map.get("applyId"),
                    (Integer) map.get("auditState"),
                    (String) map.get("rejectReason")
            );
            return ServerResponse.success("驳回成功");
        }
    }


    @DeleteMapping("/deleteby/{applyId}")
    public ServerResponse deleteByIdAdmin(@PathVariable("applyId") Integer applyId) {
        int rowsAffected = RecordJDBC.deleteRecordById(applyId);

        if (rowsAffected > 0) {
            return ServerResponse.success("删除成功");
        } else {
            return ServerResponse.fail("删除失败");
        }
    }




}




