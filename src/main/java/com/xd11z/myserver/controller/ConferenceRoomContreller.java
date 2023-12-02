package com.xd11z.myserver.controller;

import com.xd11z.myserver.entity.*;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//Get请求均可通过"http://localhost:8080/XXX"直接查看后端的回复情况（需要参数）
//返回体固定为ServerResponse类型，在data中定义

@RestController
@RequestMapping("/conference-room")
public class ConferenceRoomContreller 
{
    @GetMapping("/listall")//展示所有会议室
    //返回值为ConferenceRoom的列表
    public ServerResponse getAllConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = Arrays.asList
        (
                new ConferenceRoom("114514", "大会议室tyyyy", "1", 50, 1),
                new ConferenceRoom("1919810", "asdad", "2", 20, 1)
        );
        return ServerResponse.success(conferenceRooms);
    }

    @GetMapping("/getconditions")//展示会议室管理页面的下拉搜索框
    //返回值为三个列表 分别为所有可能的序号、类型、容纳人数数据
    public ServerResponse getconditions()
    {
        List<Map<String,Integer>> floors = new ArrayList<>();
        List<Map<String,String>> types = new ArrayList<>();
        List<Map<String,Integer>> sizes = new ArrayList<>();
        return ServerResponse.success(new Conditions(floors,types,sizes));
    }

    @PutMapping(value = "/changestate")//管理员修改会议室的可用状态
    public ServerResponse changeState(@RequestBody ConferenceRoom conferenceRoom)
    {  
        
        boolean b=true;
        if(b) return ServerResponse.success("修改成功");
        else return ServerResponse.fail("修改失败");
    }
}