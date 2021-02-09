package com.tgfc.tw.controller;


import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.UserDailyRecordRequest;
import com.tgfc.tw.entity.model.response.user.UserOrderRecordGroupResponse;
import com.tgfc.tw.entity.model.response.user.UserOrderRecordListResponse;
import com.tgfc.tw.service.UserAllRecordService;
import com.tgfc.tw.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("api/record")
public class UserRecordController {

   @Autowired
   UserRecordService userRecordService;

   @Autowired
   UserAllRecordService userAllRecordService;


    @GetMapping("userOrderWeek")
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    public UserOrderRecordListResponse getUserOrderWeekRecord(){
        return userRecordService.getUserOrderWeekRecord();
    }

    @PostMapping("userAllOrderRecord")
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    public Page<UserOrderRecordGroupResponse> getUserAllRecord(@RequestBody UserDailyRecordRequest model){
        return userAllRecordService.getUserAllRecord(model);
    }
}
