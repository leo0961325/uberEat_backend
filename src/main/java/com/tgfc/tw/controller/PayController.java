package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.AddStoreValueRequest;
import com.tgfc.tw.entity.model.response.pay.StoreValueRecordResponse;
import com.tgfc.tw.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("api/storeValue/v2.1")
public class PayController {

    @Autowired
    PayService payService;

    @RolesAllowed( PermissionEnum.Role.MANAGER)
    @PostMapping("/addStoreValue")
    public void addStoreValue(@RequestBody @Valid AddStoreValueRequest request) throws Exception {
        payService.addStoreValue(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("/getStoreValueRecordList")
    public StoreValueRecordResponse getStoreValueRecordList(int memberId, int pageNumber, int pageSize, boolean sortMode) throws Exception {
        return payService.getStoreValueRecordList(memberId, pageNumber, pageSize, sortMode);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("/getBalance")
    public Map getBalance() throws Exception {
        return payService.getBalance();
    }
}
