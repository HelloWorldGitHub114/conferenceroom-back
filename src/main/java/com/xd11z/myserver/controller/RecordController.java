package com.xd11z.myserver.controller;

import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.annotation.UserLoginToken;
import com.xd11z.myserver.entity.*;
import com.xd11z.myserver.util.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
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
    public ServerResponse listByConditions(@PathVariable("auditState") Integer auditState,
                                           @PathVariable("deleted") Integer deleted) {
        try {
            int num = RecordJDBC.getTotalnumber(auditState, deleted);
            return ServerResponse.success(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ServerResponse.fail("查询失败！");
        }
    }

}




