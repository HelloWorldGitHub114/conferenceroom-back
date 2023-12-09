package com.xd11z.myserver.controller;

import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.entity.ConRApplyRecord;
import com.xd11z.myserver.entity.ServerResponse;
import com.xd11z.myserver.util.RecordJDBC;
import com.xd11z.myserver.util.logger;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
@RestController
@RequestMapping("/apply")
public class ApplyController {
    @PostMapping("/add")
    public ServerResponse add(@RequestBody ConRApplyRecord record){
        logger.write(JSON.toJSONString(record));
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
            @PathVariable("startTime") String startTimeStr,
            @PathVariable("endTime") String endTimeStr) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();

            // 检查开始时间是否早于当前时间
            if (startTime.isBefore(now)) {
                return ServerResponse.fail("开始时间不能早于当前时间！");
            }
            if (endTime.isBefore(startTime)) {
                return ServerResponse.fail("结束时间不能早于开始时间！");
            }

            System.out.println("RoomId: " + roomId);
            System.out.println("StartTime: " + startTime);
            System.out.println("EndTime: " + endTime);

            List<Integer> listApplyId = RecordJDBC.searchTimeConflict(roomId, startTime, endTime);

            if (listApplyId.size() > 0) {
                return ServerResponse.fail("与别的申请有冲突，请换一个时间段或者会议室进行申请！");
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
