package com.xd11z.myserver.controller;


import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.entity.*;
import com.xd11z.myserver.util.*;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @PostMapping("/add")
    public ServerResponse addOrUpdate(@RequestBody Device device) {
        try {
            // 从请求体中获取设备ID、设备名称和所属会议室ID
            Integer deviceId = device.getId();
            String deviceName = device.getDname();
            Integer roomId = device.getRoomId();
            System.out.println(deviceId);
            System.out.println(deviceName);
            System.out.println(roomId);
            // 空值检查
            if (deviceName == null || roomId == null) {
                return ServerResponse.fail("设备名称或所属会议室ID为空！");
            }

            // 如果存在设备ID，则执行更新操作；否则执行插入操作
            if (deviceId != null) {
                // 更新设备数量
                int rowsAffected = DeviceJDBC.updateDevice(device);

                if (rowsAffected > 0) {
                    return ServerResponse.success("设备插入成功！");
                } else {
                    return ServerResponse.fail("设备插入失败！");
                }
            } else {
                // 插入新设备
                int rowsAffected = DeviceJDBC.insertDevice(device);

                if (rowsAffected > 0) {
                    return ServerResponse.success("设备插入成功！");
                } else if (rowsAffected == -1) {
                    return ServerResponse.fail("该会议室已存在此设备，请直接修改设备数量即可");
                } else {
                    return ServerResponse.fail("设备插入失败！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.fail("发生错误：" + e.getMessage());
        }
    }


    @GetMapping("/listby/{roomId}")
    public ServerResponse listByRoomId(@PathVariable("roomId") Integer roomId){
        List<Device> devices = DeviceJDBC.listByRoomid(roomId);
        //System.out.println(devices);
        return ServerResponse.success(devices);
    }

    @PutMapping("/changenumber")
    public ServerResponse changeNumber(@RequestBody Map<String, Object> map) {
        try {

            // 从请求体中获取设备ID和新的设备数量
            Integer deviceId = (Integer) map.get("did");
            Integer newNumber = (Integer) map.get("number");

            // 空值检查
            if (deviceId == null || newNumber == null) {
                return ServerResponse.fail("设备ID或设备数量为空！");
            }

            // 调用封装好的 JDBC 函数执行更新操作
            int rowsAffected = DeviceJDBC.updateDeviceNumber(deviceId, newNumber);

            if (rowsAffected > 0) {
                return ServerResponse.success("设备数量更新成功！");
            } else {
                return ServerResponse.fail("设备数量更新失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.fail("发生错误：" + e.getMessage());
        }
    }



}
