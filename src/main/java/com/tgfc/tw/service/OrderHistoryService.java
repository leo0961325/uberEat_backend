package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.response.InitGroupUserResponse;
import com.tgfc.tw.entity.model.response.InitGroupUserResponseV2_1;
import com.tgfc.tw.entity.model.response.InitOrderResponseV2_1;
import com.tgfc.tw.entity.model.response.orderHistory.*;
import com.tgfc.tw.entity.model.response.user.UserListByRemoveOrderResponseV2_1;
import com.tgfc.tw.entity.model.response.user.UserListResponse;
import com.tgfc.tw.service.exception.FloorIdNullException;

import java.util.List;

public interface OrderHistoryService {

    //進入團-點餐模式
    @Deprecated
    OrderHistoryGroupResponse getGroup(int groupId) throws FloorIdNullException;
    //V2_1
    OrderHistoryGroupResponseV2_1 getGroupV2_1(int groupId) throws FloorIdNullException;
    //點餐列表
    List<OrderHistoryCountOrderResponseV2_1> getGroupByOrderV2_1(int groupId, int storeId, List<Integer> teamIdList);
    @Deprecated
    List<OrderHistoryCountOrderResponse> getGroupByOrder(int groupId,int storeId, Integer floorId);
    List<OrderHistoryCountUserResponse> getGroupByUser(int groupId, Integer storeId, int floorId, Integer userId);
    List<OrderHistoryCountUserResponse> getGroupByOrderUser(int groupId, List<Integer> storeId, Integer teamId, Integer userId);
    List<OrderHistoryCountUserResponseV2_1> getGroupByOrderUserV2_1(int groupId, List<Integer> storeId, List<Integer> teamIdList, Integer userId);

    //變更點參選項
    @Deprecated
    void addOrderItem(int optionId,int groupId)throws Exception;
    @Deprecated
    void delOrderItem(int orderId,int groupId)throws Exception;


    //管理人加點
    void managerAddOrder(int optionId,int groupId,int userId)throws Exception;
    //管理人刪除餐點
    void managerDeleteOrder(Integer OptionId,int groupId,int userId)throws Exception;

    //新增/刪除PASS
    void changeOrderPass(int groupId, int storeId) throws Exception;

    //點餐人員列表
    List<UserListResponse> getOrderUserList(int optionId, int groupId);

	//點餐人員列表v2.1
	List<UserListByRemoveOrderResponseV2_1> getOrderUserListV2_1(int productId, int groupId);


    InitGroupUserResponse getInitDataByUser(int groupId);

    InitGroupUserResponseV2_1 getInitDataByUserV2_1(int groupId);

    InitOrderResponse getOrderInitData(int groupId);

    InitOrderResponseV2_1 getOrderInitDataV2_1(int groupId);
    //取得團點餐資料(僅餐點項目資料)
    GroupOrderItemResponse getGroupOrderItem(int groupId, int storeId, String keyword, int pageNumber, int pageSize, Integer orderType, Integer sortType) throws Exception;
}
