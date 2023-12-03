package com.xd11z.myserver.controller;

import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.entity.*;
import com.xd11z.myserver.util.*;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

//Get请求均可通过"http://localhost:8080/XXX"直接查看后端的回复情况（需要参数）
//返回体固定为ServerResponse类型，在data中定义

@RestController
@RequestMapping("/conference-room")
public class ConferenceRoomContreller 
{
    @GetMapping("/listall")//展示所有会议室
    //返回值为ConferenceRoom的列表
    public ServerResponse getAllConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = ConferenceRoomJDBC.SearchAll(0);
        return ServerResponse.success(conferenceRooms);
    }

    @GetMapping("listallonstate")//展示所有可用（used=1）的会议室
    public  ServerResponse Getallonstate()
    {
        List<ConferenceRoom> conferenceRooms = ConferenceRoomJDBC.SearchAll(1);
        return ServerResponse.success(conferenceRooms);
    }

    @GetMapping("getconditionsonstate")//展示申请会议室页面的下拉搜索框（只返回可用状态为1的）
    public ServerResponse GetConditionsOnstate()
    {
        List<Map<String,Integer>> floors = ConferenceRoomJDBC.GetDistinctFloor(1);
        List<Map<String,String>> types = ConferenceRoomJDBC.GetDistinctType(1);
        List<Map<String,Integer>> sizes = ConferenceRoomJDBC.GetDistinctSize(1);
        return ServerResponse.success(new Conditions(floors,types,sizes));
    }

    @GetMapping("/getconditions")//展示会议室管理页面的下拉搜索框
    //返回值为三个列表 分别为所有可能的序号、类型、容纳人数数据
    public ServerResponse getconditions()
    {
        List<Map<String,Integer>> floors = ConferenceRoomJDBC.GetDistinctFloor(0);
        List<Map<String,String>> types = ConferenceRoomJDBC.GetDistinctType(0);
        List<Map<String,Integer>> sizes = ConferenceRoomJDBC.GetDistinctSize(0);
        return ServerResponse.success(new Conditions(floors,types,sizes));
    }

    @PutMapping(value = "/changestate")//管理员修改会议室的可用状态
    public ServerResponse changeState(@RequestBody ConferenceRoom conferenceRoom)
    {
        boolean flg=ConferenceRoomJDBC.ChangeState(conferenceRoom);
        if(flg) return ServerResponse.success("修改成功");
        else return ServerResponse.fail("修改失败");
    }

    @PostMapping("/add")//添加会议室
    //未实现
    public ServerResponse AddOrUpdate(@RequestBody ConferenceRoom conferenceRoom)
    {
        ConferenceRoomJDBC.ChangeState(conferenceRoom);//设置为可用
        logger.write(JSON.toJSONString(conferenceRoom));
        return ServerResponse.success("");
    }

    @GetMapping("/listby/{roomFloor}/{roomType}/{roomSize}")
    public ServerResponse listAll(@PathVariable(value = "roomFloor")  String roomFloor,
                                  @PathVariable(value = "roomType")  String roomType,
                          @PathVariable(value = "roomSize")  String roomSize)
    {
        roomFloor  = String.valueOf(JSON.parse(roomFloor));
        roomType = (String)JSON.parse(roomType);
        roomSize = String.valueOf(JSON.parse(roomSize));
        List<ConferenceRoom> res = ConferenceRoomJDBC.SearchOnConditon(roomFloor,roomType,roomSize);
        return ServerResponse.success(res);
    }
}