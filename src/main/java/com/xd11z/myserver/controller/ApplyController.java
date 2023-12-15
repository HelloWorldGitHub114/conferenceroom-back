package com.xd11z.myserver.controller;
import com.alibaba.fastjson.JSON;

import com.xd11z.myserver.entity.*;
import com.xd11z.myserver.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.ZoneOffset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.temporal.ChronoUnit;
@RestController
@RequestMapping("/apply")
public class ApplyController {
    @PostMapping("/add")
    public ServerResponse add(@RequestBody ConRApplyRecord record){
        try{
            int applyID = RecordJDBC.addRecord(record);
            if(applyID>0){
                return ServerResponse.success("申请成功！");
            }
            else{
                return ServerResponse.fail("申请失败！");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return ServerResponse.fail("申请添加失败，发生异常：" + e.getMessage());
        }
    }

    //有问题，不管什么时间段都会冲突
    @GetMapping("/searchtimeconflict/{roomId}/{startTime}/{endTime}")
    public ServerResponse searchtimeconflict(
            @PathVariable("roomId") Integer roomId,
            @PathVariable("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @PathVariable("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        try {
            LocalDateTime now = LocalDateTime.now();

            // 检查开始时间是否早于当前时间
            if (startTime.isBefore(now)) {
                return ServerResponse.fail("开始时间不能早于当前时间！");
            }

            System.out.println("RoomId: " + roomId);
            System.out.println("StartTime: " + startTime);
            System.out.println("EndTime: " + endTime);

            List<Integer> listApplyId = RecordJDBC.searchTimeConflict(roomId, startTime, endTime);

            if (listApplyId.size() > 0) {
                return ServerResponse.success("0");
            } else {
                return ServerResponse.success("1");
            }
        } catch (DateTimeParseException e) {
            // 处理日期时间解析异常
            e.printStackTrace();
            return ServerResponse.fail("日期时间格式错误");
        }
    }



}
