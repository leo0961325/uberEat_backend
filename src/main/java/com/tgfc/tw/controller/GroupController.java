package com.tgfc.tw.controller;

import com.tgfc.tw.controller.exception.ErrorCodeException;
import com.tgfc.tw.controller.exception.enums.GroupErrorCode;
import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.AddGroupRequest;
import com.tgfc.tw.entity.model.request.GroupRequest;
import com.tgfc.tw.entity.model.request.GroupWithoutIdRequest;
import com.tgfc.tw.entity.model.response.*;
import com.tgfc.tw.service.GroupService;
import com.tgfc.tw.service.exception.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("api/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    /**
     * Version V2.1
     */
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("v2.1/timeList")
    public List<GroupResponseV2_1> getGroupListV2_1(@RequestParam(required = false) List<Integer> tagId, @RequestParam(required = false) String keyword, @RequestParam(required = false) List<Integer> teamId) {
        return groupService.getGroupListV2_1(tagId, keyword, teamId);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping(value = "add")
    public void insertGroup(@RequestBody GroupWithoutIdRequest model) throws Exception {
        groupService.insertGroup(model);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping(value = "v2.1/add")
    public void addGroup(@RequestBody AddGroupRequest request) throws Exception {
        groupService.addGroup(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping("update")
    public void updateGroup(@RequestBody GroupRequest model) throws Exception {
        groupService.updateGroup(model);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping("delete")
    public void deleteGroup(@RequestParam int id) throws Exception {
        groupService.deleteGroup(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping("v2.1/delete")
    public void deleteGroupV2_1(@RequestParam int id) {
        groupService.deleteGroupV2_1(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("list")
    public Page<GroupResponse> getByTypeAndKeyword(@RequestParam String keyword, @RequestParam boolean isOwner, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return groupService.getByTypeAndKeyword(keyword, isOwner, pageNumber, pageSize);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("timeList")
    public List<GroupTimeResponse> getGroupList(@RequestParam(required = false) List<Integer> tagId, @RequestParam(required = false) String keyword, @RequestParam(required = false) List<Integer> floorId) {
        return groupService.getGroupList(tagId, keyword, floorId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("getInitData")
    public List<InitResponse> getInitData() {
        return groupService.getInitData();
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping("lock")
    public void lock(@RequestParam int groupId, @RequestParam Boolean status) throws Exception {
        groupService.lock(groupId, status);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping("v2.1/lock")
    public void lockV2_1(@RequestParam int groupId, @RequestParam boolean status) {
        groupService.lockV2_1(groupId, status);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("get")
    public GroupItemResponse getOne(@RequestParam int id) throws Exception {

        try {
            return groupService.getOne(id);
        } catch (IdNotFoundException e) {
            throw new ErrorCodeException(GroupErrorCode.GROUP_NOT_FOUND, e.getMessage());
        }
    }

    //取得下拉式選單資料
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "floorSelect")
    public List<FloorResponse> getFloorItemData() {
        return groupService.getFloorItemData();
    }

    //取得團管理員(選取對應樓層後)

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "v2.1/getGroupManager")
    public List<GroupTeamUserResponse> getGroupManagerV2_1() {
        return groupService.getGroupManagerV2_1();
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "getGroupManager")
    public List<GroupFloorUserResponse> getGroupManager() {
        return groupService.getGroupManager();
    }

}