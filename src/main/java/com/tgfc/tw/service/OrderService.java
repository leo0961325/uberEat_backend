package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.OrderWithoutIdRequest;
import com.tgfc.tw.entity.model.request.OrderWithoutStoreIdRequest;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.order.OrderResponse;
import com.tgfc.tw.entity.model.response.StoreResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponseV2_1;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    OrderResponse getOrderById(int orderId) throws Exception;

    Page<OrderResponse> orderList(int storeId, int pageNumber, int pageSize, String keyword);

    void add(OrderWithoutIdRequest model) throws Exception;

    void addNew(OrderWithoutIdRequest model) throws Exception;

    void update(OrderWithoutStoreIdRequest model) throws Exception;

    void managerUpdate(OrderWithoutStoreIdRequest model) throws Exception;

    void delete(int id) throws Exception;

    void managerDelete(int id) throws Exception;

    List<StoreResponse> getStoreList();

    List<StoreResponse> getStoreList(int groupId);

    List<FloorResponse> getFloorList();

    @Deprecated
    List<UserNameResponse> getUserList(Integer groupId);

    List<UserNameResponseV2_1> getUserListV2_1(Integer groupId);

    void addList(List<OrderWithoutIdRequest> requests) throws Exception;

    void addNewList(List<OrderWithoutIdRequest> requests) throws Exception;
}
