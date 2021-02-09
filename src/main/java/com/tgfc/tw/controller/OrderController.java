package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.OrderWithoutIdRequest;
import com.tgfc.tw.entity.model.request.OrderWithoutStoreIdRequest;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.order.OrderResponse;
import com.tgfc.tw.entity.model.response.StoreResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponseV2_1;
import com.tgfc.tw.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "list")
    public Page<OrderResponse> getList(@RequestParam int storeId, @RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false) String keyword) {
        return orderService.orderList(storeId, pageNumber, pageSize, keyword);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping(value = "add")
    public void addOrder(@Valid @RequestBody OrderWithoutIdRequest model) throws Exception {
        orderService.add(model);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping(value = "addList")
    public void addOrderList(@Valid @RequestBody List<OrderWithoutIdRequest> requests) throws Exception {
        orderService.addList(requests);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PutMapping(value = "update")
    public void updateOrder(@Valid @RequestBody OrderWithoutStoreIdRequest model) throws Exception {
        orderService.update(model);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping(value = "managerUpdate")
    public void managerUpdateOrder(@Valid @RequestBody OrderWithoutStoreIdRequest model) throws Exception {
        orderService.managerUpdate(model);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @DeleteMapping(value = "delete")
    public void removeOrder(@RequestParam int id) throws Exception {
        orderService.delete(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping(value = "managerDelete")
    public void managerRemoveOrder(@RequestParam int id) throws Exception {
        orderService.managerDelete(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping(value = "get")
    public OrderResponse getOrder(@RequestParam int orderId) throws Exception {
        return orderService.getOrderById(orderId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("getStoreList")
    public List<StoreResponse> getStoreList(@RequestParam(required = false) Integer groupId) {
        if (groupId != null)
            return orderService.getStoreList(groupId);
        else
            return orderService.getStoreList();
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("getFloorList")
    public List<FloorResponse> getFloorList() {
        return orderService.getFloorList();
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("getUserList")
    public List<UserNameResponse> getUserList(@RequestParam(required = false) Integer groupId) {
        return orderService.getUserList(groupId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("v2.1/getUserList")
    public List<UserNameResponseV2_1> getUserListV2_1(@RequestParam(required = false) Integer groupId) {
        return orderService.getUserListV2_1(groupId);
    }

}
