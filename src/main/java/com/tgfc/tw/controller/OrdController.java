package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.OptionAddRequestV2_1;
import com.tgfc.tw.service.OrdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("api/ord")
public class OrdController {

    @Autowired
    OrdService ordService;

    @RolesAllowed({PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PutMapping(value = "v2.1/put")
    public void addOrderItem(@RequestParam int optionId, @RequestParam int groupId) {
        ordService.addOrderItem(optionId, groupId);
    }

    @RolesAllowed({PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PutMapping(value = "v2.1/remove")
    public void delOrderItem(@RequestParam int productId, @RequestParam int groupId) {
        ordService.delOrderItem(productId, groupId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping(value = "v2.1/managerAdd")
    public void managerAddOrder(@RequestParam int optionId, @RequestParam int groupId, @RequestParam int userId) {
        ordService.managerAddOrder(optionId, groupId, userId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping(value = "v2.1/managerDelete")
    public void managerDeleteOrder(@RequestParam int productId, @RequestParam int groupId, @RequestParam int userId) {
        ordService.managerDeleteOrder(productId, groupId, userId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PostMapping("v2.1/addOption")
    public void addOneOptionAndOrder(@Valid @RequestBody OptionAddRequestV2_1 request) {
        ordService.addOptionAndOrder(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping("v2.1/managerAddOption")
    public void managerAddOneOptionAndOrder(@Valid @RequestBody OptionAddRequestV2_1 request) {
        ordService.managerAddOptionAndOrder(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping("v2.1/removeOption")
    public void deleteOption(@RequestParam int optionId, @RequestParam int groupId) {
        ordService.deleteOption(optionId, groupId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER})
    @PostMapping("initPay")
    public void initPayRecord() {
        ordService.initPayRecord();
    }
}
