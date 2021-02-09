package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.UserPayRequest;
import com.tgfc.tw.entity.model.request.SearchUserRequest;
import com.tgfc.tw.entity.model.request.UserPayTotalRequest;
import com.tgfc.tw.entity.model.request.UserUpdateRequest;
import com.tgfc.tw.entity.model.response.RoleResponse;
import com.tgfc.tw.entity.model.response.user.*;
import com.tgfc.tw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    UserService userService;


    /**
     * Version V2.1
     */
    @GetMapping("v2.1/get")
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    public UserDevResponseV2_1 getV2_1(@RequestParam int id) {
        return userService.getUserV2_1(id);
    }

    /**
     * Version V2.1
     */
    @GetMapping("v2.1/list")
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    public UserListResponseV2_1 listV2_1(@RequestParam(required = false) String keyword, @RequestParam(required = false) List<Integer> teamIdList, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return userService.getUserListV2_1(keyword, teamIdList, pageNumber, pageSize);
    }

    /**
     * Version V2.1
     */
    @PutMapping("v2.1/update")
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    public void updateV2_1(@Valid @RequestBody UserUpdateRequestV2_1 request) {
        userService.updateV2_1(request);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("get")
    public UserDevResponse get(@RequestParam int id) {
        return userService.getUser(id);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("list")
    public Page<UserListResponse> list(@RequestParam(required = false) String keyword, @RequestParam int pageNumber, @RequestParam int pageSize) {
        SearchUserRequest searchUserRequest = new SearchUserRequest();
        searchUserRequest.setKeyword(keyword);
        searchUserRequest.setPageNumber(pageNumber);
        searchUserRequest.setPageSize(pageSize);
        return userService.getUserList(searchUserRequest);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping("update")
    public void update(@Valid @RequestBody UserUpdateRequest request) {
        userService.update(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping("addLdapUsers")
    public void addUserByLdap(@RequestParam List<String> accounts) {
        userService.addUserByLdap(accounts);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("getPermission")
    public List<String> getPermission() {
        return userService.getPermission();
    }

//    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
//    @PostMapping(value = "pay")
//    public Boolean payStatus(@RequestBody UserPayRequest model)throws Exception{
//       return userService.insertGroupPay(model);
//    }

//    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
//    @PutMapping(value = "payTotal")
//    public Boolean payTotal(@RequestBody UserPayTotalRequest model)throws Exception{
//        return userService.insertGroupPayTotal(model);
//    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("getRole")
    public List<RoleResponse> getRole() {
        return userService.getRole();
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("v2.1/getAllUserList")
    public List<UserAllListResponse> getAllUserList() {
        return userService.getAllUserList();
    }


}
