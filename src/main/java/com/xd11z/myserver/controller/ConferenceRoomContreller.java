package com.xd11z.myserver.controller;

import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.annotation.UserToken;
import com.xd11z.myserver.entity.Conditions;
import com.xd11z.myserver.entity.ConferenceRoom;
import com.xd11z.myserver.entity.ConferenceRoomForm;
import com.xd11z.myserver.entity.ServerResponse;
import com.xd11z.myserver.util.ConferenceRoomJDBC;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

//Get请求均可通过"http://localhost:8080/XXX"直接查看后端的回复情况（需要参数）
//返回体固定为ServerResponse类型，在data中定义

@RestController
@RequestMapping("/conference-room")
public class ConferenceRoomContreller 
{
    @UserToken
    @GetMapping("/listall")//展示所有会议室
    //返回值为ConferenceRoom的列表
    public ServerResponse getAllConferenceRooms()
    {
        List<ConferenceRoom> conferenceRooms = ConferenceRoomJDBC.SearchAll(2);
        return ServerResponse.success(conferenceRooms);
    }

    @UserToken
    @GetMapping("listallonstate")//展示所有会议室（可用）
    public  ServerResponse Getallonstate()
    {
        List<ConferenceRoom> conferenceRooms = ConferenceRoomJDBC.SearchAll(1);
        return ServerResponse.success(conferenceRooms);
    }

    @UserToken
    @GetMapping("getconditionsonstate")//展示申请会议室页面的下拉搜索框（只返回可用的）
    public ServerResponse GetConditionsOnstate()
    {
        List<Map<String,Integer>> floors = ConferenceRoomJDBC.GetDistinctFloor(1);
        List<Map<String,Integer>> sizes = ConferenceRoomJDBC.GetDistinctSize(1);
        return ServerResponse.success(new Conditions(floors,sizes));
    }

    @UserToken
    @GetMapping("/getconditions")//展示会议室管理页面的下拉搜索框
    //返回值为两个列表 分别为所有可能的序号、容纳人数数据
    public ServerResponse getconditions()
    {
        List<Map<String,Integer>> floors = ConferenceRoomJDBC.GetDistinctFloor(2);
        List<Map<String,Integer>> sizes = ConferenceRoomJDBC.GetDistinctSize(2);
        return ServerResponse.success(new Conditions(floors,sizes));
    }

    @UserToken(role ="admin")
    @PutMapping(value = "/changestate")//管理员修改会议室的可用状态
    public ServerResponse changeState(@RequestBody ConferenceRoom conferenceRoom)
    {
        boolean flg=ConferenceRoomJDBC.ChangeState(conferenceRoom);
        if(flg) return ServerResponse.success("1");
        else return ServerResponse.fail("修改失败");
    }

    @UserToken(role = "admin")
    @PostMapping("/add")//添加和更新会议室信息
    public ServerResponse Add(@RequestParam("jsonData") String jsonData,
                              @RequestParam(value = "photo",required = false) MultipartFile photo)
    {
        ConferenceRoomForm conferenceRoomForm = JSON.parseObject(jsonData, ConferenceRoomForm.class);
        int roomID=conferenceRoomForm.roomID;
        String roomNo=conferenceRoomForm.roomNo;//修改之后的No
        boolean flg=false;
        boolean repeatID=ConferenceRoomJDBC.CheckRepeatID(roomID);//检查数据库中是否存在该ID
        if(repeatID==true)//说明为修改 房号可以等于自己之前的房号
        {
            boolean repeatNo=ConferenceRoomJDBC.CheckRepeatNo(roomNo);//房号是否重复
            boolean repeatMyself=(conferenceRoomForm.roomNo.equals(ConferenceRoomJDBC.SearchByID(roomID).roomNo));//房号是否与自己修改前重复
            if((repeatNo) && (!repeatMyself)) return ServerResponse.fail("房号重复，请修改房号。");
            else flg=ConferenceRoomJDBC.UpdateInfo(conferenceRoomForm,photo);
        }
        else//说明为新增 房号不能重复
        {
            if(ConferenceRoomJDBC.CheckRepeatNo(roomNo)) return ServerResponse.fail("房号重复，请修改房号。");
            flg=ConferenceRoomJDBC.InsertInfo(conferenceRoomForm,photo);
        }
        if(!flg) return ServerResponse.fail("操作错误");
        else return ServerResponse.success("");
    }

    @GetMapping("/listbyonstate")//按照条件筛选会议室（默认可用）
    public ServerResponse listAllOnState(@RequestParam(required = false) String roomFloor,
                                         @RequestParam(required = false) String roomSize)
    {
        if(roomFloor==null) roomFloor="";
        if(roomSize==null) roomSize="";
        List<ConferenceRoom> res = ConferenceRoomJDBC.SearchOnConditon(roomFloor,roomSize,1);
        return ServerResponse.success(res);
    }

    @GetMapping("/listby")//按照条件筛选会议室
    public ServerResponse listAll(@RequestParam(required = false) String roomFloor,
                                  @RequestParam(required = false) String roomSize,
                                  @RequestParam(required = false) String roomState)
    {
        if(roomFloor==null) roomFloor="";
        if(roomSize==null) roomSize="";
        if(roomState==null) roomState="2";//默认为全搜索
        List<ConferenceRoom> res = ConferenceRoomJDBC.SearchOnConditon(roomFloor,roomSize,Integer.parseInt(roomState));
        return ServerResponse.success(res);
    }

    @DeleteMapping("/delete/{roomID}")//删除会议室
    public ServerResponse DeleteRoom(@PathVariable(value = "roomID") int ID) {
        boolean flg = ConferenceRoomJDBC.DeleteRoom(ID);
        if (!flg) return ServerResponse.fail("删除错误");
        else return ServerResponse.success("");
    }
}