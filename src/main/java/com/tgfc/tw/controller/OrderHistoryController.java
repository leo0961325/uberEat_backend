package com.tgfc.tw.controller;

import com.tgfc.tw.controller.exception.ErrorCodeException;
import com.tgfc.tw.controller.exception.enums.OrderHistoryErrorCode;
import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.OrderWithoutIdRequest;
import com.tgfc.tw.entity.model.response.InitGroupUserResponse;
import com.tgfc.tw.entity.model.response.InitGroupUserResponseV2_1;
import com.tgfc.tw.entity.model.response.InitOrderResponseV2_1;
import com.tgfc.tw.entity.model.response.orderHistory.*;
import com.tgfc.tw.entity.model.response.user.UserListByRemoveOrderResponseV2_1;
import com.tgfc.tw.entity.model.response.user.UserListResponse;
import com.tgfc.tw.service.OrderService;
import com.tgfc.tw.service.StorePictureService;
import com.tgfc.tw.service.exception.FloorIdNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderHistoryController {

    @Autowired
    com.tgfc.tw.service.OrderHistoryService OrderHistoryService;

    @Autowired
    OrderService OrderService;

    @Autowired
    StorePictureService storePictureService;


    //取得團點餐資料(不含餐點項目資料)
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("api/orderHistory/get")
    public OrderHistoryGroupResponse getGroup(@RequestParam int groupId) throws ErrorCodeException {
        try {
            return OrderHistoryService.getGroup(groupId);
        } catch (FloorIdNullException e) {
            throw new ErrorCodeException(OrderHistoryErrorCode.FLOOR_ID_NOT_SETTING, e.getMessage());
        }
    }

    //取得團點餐資料(僅含團資料)_v2.1
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("api/orderHistory/v2.1/get")
    public OrderHistoryGroupResponseV2_1 getGroupV2(@RequestParam int groupId) throws ErrorCodeException {
        try {
            return OrderHistoryService.getGroupV2_1(groupId);
        } catch (FloorIdNullException e) {
            throw new ErrorCodeException(OrderHistoryErrorCode.FLOOR_ID_NOT_SETTING, e.getMessage());
        }
    }

    //取得團點餐資料(僅餐點項目資料)
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("api/orderHistory/getGroupOrderItem")
    public GroupOrderItemResponse getGroupOrderItem(@RequestParam int groupId, @RequestParam int storeId, @RequestParam String keyword,
                                                    @RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false) Integer orderType,
                                                    @RequestParam(required = false) Integer sortType) throws Exception {
        return OrderHistoryService.getGroupOrderItem(groupId, storeId, keyword, pageNumber, pageSize, orderType, sortType);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("api/orderHistory/v2.1/getGroupByOrder")
    public List<OrderHistoryCountOrderResponseV2_1> getGroupByOrderV2_1(@RequestParam int groupId,@RequestParam int storeId,@RequestParam(required = false) List<Integer> teamIdList){
        return OrderHistoryService.getGroupByOrderV2_1(groupId,storeId,teamIdList);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("api/orderHistory/getGroupByOrder")
    public List<OrderHistoryCountOrderResponse> getGroupByOrder(@RequestParam int groupId,@RequestParam int storeId,@RequestParam(required = false) Integer floorId){
        return OrderHistoryService.getGroupByOrder(groupId,storeId,floorId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("api/orderHistory/getGroupByUser")
    public List<OrderHistoryCountUserResponse> getGroupByUser(@RequestParam int groupId,@RequestParam(required = false) Integer storeId,@RequestParam int floorId, @RequestParam(required = false) Integer userId){
        return OrderHistoryService.getGroupByUser(groupId, storeId, floorId, userId);
    }

    //引用Order新增點餐選項
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PostMapping(value = "api/orderHistory/post")
    public void addOrder(@Valid @RequestBody OrderWithoutIdRequest model) throws Exception {
        OrderService.add(model);
    }

    //引用Order新增點餐選項(新增加GroupId)
    @RolesAllowed({PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PostMapping(value = "api/orderHistory/postNew")
    public void addOrderNew(@Valid @RequestBody OrderWithoutIdRequest model) throws Exception {
        OrderService.addNew(model);
    }

    @RolesAllowed({PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PostMapping(value = "api/orderHistory/postNewList")
    public void addOrderNewList(@Valid @RequestBody List<OrderWithoutIdRequest> models) throws Exception {
        OrderService.addNewList(models);
    }

    //餐點+1
    @Deprecated
    @RolesAllowed({PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PutMapping(value = "api/orderHistory/put")
    public void addOrderItem(@RequestParam int optionId, @RequestParam int groupId) throws Exception {
        OrderHistoryService.addOrderItem(optionId, groupId);
    }

    //餐點刪除
    @Deprecated
    @RolesAllowed({PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PutMapping(value = "api/orderHistory/remove")
    public void delOrderItem(@RequestParam int orderId, @RequestParam int groupId) throws Exception {
        OrderHistoryService.delOrderItem(orderId, groupId);
    }


    //加點
    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping(value = "api/orderHistory/managerAdd")
    public void managerAddOrder(@RequestParam int optionId, @RequestParam int groupId,@RequestParam int userId) throws Exception{
        OrderHistoryService.managerAddOrder(optionId, groupId,userId);
    }
    //管理人刪除餐點
    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping(value = "api/orderHistory/managerDelete")
    public void managerDeleteOrder(@RequestParam(required = false) Integer orderId, @RequestParam int groupId, @RequestParam int userId) throws Exception{
        OrderHistoryService.managerDeleteOrder(orderId, groupId, userId);
    }

    @RolesAllowed({PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PutMapping(value = "api/orderHistory/changePass")
    public void changePass(@RequestParam int groupId, @RequestParam int storeId) throws Exception {
        OrderHistoryService.changeOrderPass(groupId, storeId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("api/orderHistory/getGroupByOrderUser")
    @Deprecated
    public List<OrderHistoryCountUserResponse> getGroupByOrderUser(@RequestParam int groupId,@RequestParam(required = false) List<Integer> storeId,@RequestParam(required = false) Integer floorId, @RequestParam(required = false) Integer userId){
        return OrderHistoryService.getGroupByOrderUser(groupId, storeId, floorId, userId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("api/orderHistory/v2.1/getGroupByOrderUser")
    public List<OrderHistoryCountUserResponseV2_1> getGroupByOrderUserV2_1(@RequestParam int groupId,@RequestParam(required = false) List<Integer> storeId,@RequestParam(required = false) List<Integer> teamId, @RequestParam(required = false) Integer userId){
        return OrderHistoryService.getGroupByOrderUserV2_1(groupId, storeId, teamId, userId);
    }

    //管理人取得已點餐的會員列表
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @GetMapping("api/orderHistory/getOrderUserList")
    public List<UserListResponse> getOrderUserList(@RequestParam int orderId, @RequestParam int groupId){
        return OrderHistoryService.getOrderUserList(orderId,groupId);
    }

	//管理人取得已點餐的會員列表v2.1
	@RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
	@GetMapping("api/orderHistory/v2.1/getOrderUserList")
	public List<UserListByRemoveOrderResponseV2_1> getOrderUserListV2_1(@RequestParam int productId, @RequestParam int groupId){
		return OrderHistoryService.getOrderUserListV2_1(productId,groupId);
	}

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("api/orderHistory/getInitGroupByOrderUser")
    public InitGroupUserResponse getInitDataByUser(@RequestParam int groupId){
        return OrderHistoryService.getInitDataByUser(groupId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("api/orderHistory/v2.1/getInitGroupByOrderUser")
    public InitGroupUserResponseV2_1 getInitDataByUserV2_1(@RequestParam int groupId){
        return OrderHistoryService.getInitDataByUserV2_1(groupId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("api/orderHistory/getInitDataForOrder")
    public InitOrderResponse getInitDataForOrder(@RequestParam int groupId){
        return OrderHistoryService.getOrderInitData(groupId); 
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("api/orderHistory/v2.1/getInitDataForOrder")
    public InitOrderResponseV2_1 getInitDataForOrderV2_1(@RequestParam int groupId){
        return OrderHistoryService.getOrderInitDataV2_1(groupId);
    }

}
