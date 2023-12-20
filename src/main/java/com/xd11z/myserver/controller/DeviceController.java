package com.xd11z.myserver.controller;


import com.xd11z.myserver.annotation.UserToken;
import com.xd11z.myserver.entity.Device;
import com.xd11z.myserver.entity.ServerResponse;
import com.xd11z.myserver.repository.DeviceJDBC;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @UserToken(role = "admin")
    @PostMapping("/add")
    public ServerResponse addOrUpdate(@RequestBody Device device) {
        try {
            // 从请求体中获取设备名称和所属会议室ID
            String deviceName = device.getDname();
            Integer roomId = device.getRoomId();

            // 空值检查
            if (deviceName == null || roomId == null) {
                return ServerResponse.fail("设备名称或所属会议室ID为空！");
            }

            // 如果设备ID不为空，说明是修改操作
            if (device.getId() != null) {
                // 执行修改操作
                int rowsAffected = DeviceJDBC.updateDevice(device);

                if (rowsAffected > 0) {
                    return ServerResponse.success("设备修改成功！");
                } else {
                    return ServerResponse.fail("设备修改失败！");
                }
            } else {
                // 如果设备ID为空，说明是新增操作
                // 查询设备ID
                Integer deviceId = DeviceJDBC.getDeviceIdByName(deviceName);

                // 如果存在设备ID，则说明该会议室已存在此设备，不进行插入操作
                if (deviceId != null) {
                    return ServerResponse.fail("该会议室已存在此设备，请直接修改设备数量即可！");
                } else {
                    // 插入新设备
                    int rowsAffected = DeviceJDBC.insertDevice(device);

                    if (rowsAffected > 0) {
                        return ServerResponse.success("设备插入成功！");
                    } else {
                        return ServerResponse.fail("设备插入失败！");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.fail("发生错误：" + e.getMessage());
        }
    }


    @UserToken(role = "admin")
    @DeleteMapping("/delete/{did}")
    public ServerResponse delete(@PathVariable("did") Integer did) {
        int rowsAffected = DeviceJDBC.deleteDeviceById(did);

        if (rowsAffected > 0) {
            return ServerResponse.success("设备删除成功！");
        } else {
            return ServerResponse.fail("设备删除失败！");
        }
    }


    @UserToken
    @GetMapping("/listby/{roomId}")
    public ServerResponse listByRoomId(@PathVariable("roomId") Integer roomId){
        List<Device> devices = DeviceJDBC.listByRoomid(roomId);
        return ServerResponse.success(devices);
    }
    @UserToken
    @GetMapping("/listbyapply/{roomId}")
    public ServerResponse listByRoomIdForUsers(@PathVariable("roomId") Integer roomId){
        List<Device> devices = DeviceJDBC.listByRoomidForUsers(roomId);
        return ServerResponse.success(devices);
    }

    @UserToken(role = "admin")
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
