package com.xd11z.myserver.controller;

import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.annotation.UserLoginToken;
import com.xd11z.myserver.entity.*;
import com.xd11z.myserver.util.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//Get请求均可通过"http://localhost:8080/XXX"直接查看后端的回复情况（需要参数）
//返回体固定为ServerResponse类型，在data中定义

@RestController
@RequestMapping("/conference-room")
public class ConferenceRoomContreller 
{
    @UserLoginToken
    @GetMapping("/listall")//展示所有会议室
    //返回值为ConferenceRoom的列表
    public ServerResponse getAllConferenceRooms()
    {
        List<ConferenceRoom> conferenceRooms = ConferenceRoomJDBC.SearchAll(0);
        return ServerResponse.success(conferenceRooms);
    }

    @UserLoginToken
    @GetMapping("listallonstate")//展示所有可用（used=1）的会议室
    public  ServerResponse Getallonstate()
    {
        List<ConferenceRoom> conferenceRooms = ConferenceRoomJDBC.SearchAll(1);
        return ServerResponse.success(conferenceRooms);
    }

    @UserLoginToken
    @GetMapping("getconditionsonstate")//展示申请会议室页面的下拉搜索框（只返回可用状态为1的）
    public ServerResponse GetConditionsOnstate()
    {
        List<Map<String,Integer>> floors = ConferenceRoomJDBC.GetDistinctFloor(1);
        List<Map<String,Integer>> sizes = ConferenceRoomJDBC.GetDistinctSize(1);
        return ServerResponse.success(new Conditions(floors,sizes));
    }

    @UserLoginToken
    @GetMapping("/getconditions")//展示会议室管理页面的下拉搜索框
    //返回值为两个列表 分别为所有可能的序号、容纳人数数据
    public ServerResponse getconditions()
    {
        List<Map<String,Integer>> floors = ConferenceRoomJDBC.GetDistinctFloor(0);
        List<Map<String,Integer>> sizes = ConferenceRoomJDBC.GetDistinctSize(0);
        return ServerResponse.success(new Conditions(floors,sizes));
    }

    @UserLoginToken
    @PutMapping(value = "/changestate")//管理员修改会议室的可用状态
    public ServerResponse changeState(@RequestBody ConferenceRoom conferenceRoom)
    {
        boolean flg=ConferenceRoomJDBC.ChangeState(conferenceRoom);
        if(flg) return ServerResponse.success("修改成功");
        else return ServerResponse.fail("修改失败");
    }

    @UserLoginToken
    @PostMapping("/add")//添加和更新会议室信息
    public ServerResponse Add(@RequestBody ConferenceRoom conferenceRoom)
    {
        int ID=conferenceRoom.roomID;
        String No=conferenceRoom.roomNo;//修改之后的No
        boolean flg=false;
        boolean repeatID=ConferenceRoomJDBC.CheckRepeatID(ID);//检查数据库中是否存在该ID
        if(repeatID==true)//说明为修改 房号可以等于自己之前的房号
        {
            boolean repeatNo=ConferenceRoomJDBC.CheckRepeatNo(No);//房号是否重复
            boolean repeatMyself=(conferenceRoom.roomNo.equals(ConferenceRoomJDBC.SearchByID(ID).roomNo));//房号是否与自己修改前重复
            if((repeatNo) && (!repeatMyself)) return ServerResponse.fail("房号重复，请修改房号。");
            else flg=ConferenceRoomJDBC.UpdateInfo(conferenceRoom);
        }
        else//说明为新增 房号不能重复
        {
            if(ConferenceRoomJDBC.CheckRepeatNo(No)==true) return ServerResponse.fail("房号重复，请修改房号。");
            conferenceRoom.setUseable();
            flg=ConferenceRoomJDBC.InsertInfo(conferenceRoom);
        }
        if(!flg) return ServerResponse.fail("修改错误");
        else return ServerResponse.success("");
    }

    @UserLoginToken
    @GetMapping("/listby")//按照条件筛选会议室
    public ServerResponse listAll(@RequestParam(required = false) String roomFloor,
                                  @RequestParam(required = false) String roomSize,
                                  @RequestParam(required = false) String roomState)
    {
        logger.write(roomFloor+roomSize+roomState);
        List<ConferenceRoom> res = ConferenceRoomJDBC.SearchOnConditon(roomFloor,roomSize, Integer.parseInt(roomState));
        return ServerResponse.success(res);
    }

    @DeleteMapping("/delete/")//删除会议室
    //遇到了BUG
    public ServerResponse delete(@RequestParam("roomID") int ID)
    {
        boolean flg =ConferenceRoomJDBC.DeleteRoom(ID);
        if(!flg) return ServerResponse.fail("删除错误");
        else return ServerResponse.success("");
    }
}