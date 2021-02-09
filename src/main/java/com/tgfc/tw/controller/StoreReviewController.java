package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.StoreReviewAddRequest;
import com.tgfc.tw.entity.model.response.StoreReviewResponse;
import com.tgfc.tw.entity.model.response.UserStoreReviewResponse;
import com.tgfc.tw.service.StoreReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("api/storereview")
public class StoreReviewController {

    @Autowired

    StoreReviewService storeReviewService;

    @GetMapping("list")
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    public StoreReviewResponse list(@RequestParam Integer storeId){
        return storeReviewService.list(storeId);
    }

    @GetMapping("get")
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    public UserStoreReviewResponse get(@RequestParam Integer storeId){
        return storeReviewService.get(storeId);
    }

    @PutMapping("add")
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    public void add(@RequestBody StoreReviewAddRequest request){ storeReviewService.add(request);}
}
