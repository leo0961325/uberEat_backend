package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.OptionAddRequest;
import com.tgfc.tw.entity.model.request.OptionUpdateRequest;
import com.tgfc.tw.entity.model.response.OptionCountResponse;
import com.tgfc.tw.entity.model.response.OptionResponse;
import com.tgfc.tw.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/option")
public class OptionController {

    @Autowired
    OptionService optionService;

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PostMapping("add")
    public void addOneOption(@Valid @RequestBody OptionAddRequest request) throws Exception {
        optionService.addOneOption(request);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping("managerAdd")
    public void managerAddOneOption(@Valid @RequestBody OptionAddRequest request) throws Exception {
        optionService.managerAddOneOption(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("list")
    public List<OptionResponse> getOptionByOrder(@RequestParam int orderId) {
        return optionService.getOptionByOrder(orderId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping("update")
    public void updateOption(@Valid @RequestBody OptionUpdateRequest request) throws Exception {
        optionService.updateOption(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping("delete")
    public void deleteOption(@RequestParam int id) throws Exception {
        optionService.deleteOption(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("get")
    public OptionResponse getOption(@RequestParam int optionId) throws Exception {
        return optionService.getOptionById(optionId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("plusList")
    public List<OptionCountResponse> getOptionByGroup(@RequestParam int groupId, @RequestParam int orderId) throws Exception {
        return optionService.getOptionByGroup(groupId, orderId);
    }
}
