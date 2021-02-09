package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.request.UserDailyRecordRequest;
import com.tgfc.tw.entity.model.response.OrderDetailResponse;
import com.tgfc.tw.entity.model.response.UserOrderDetailResponse;
import com.tgfc.tw.entity.model.response.user.UserOrderRecordGroupResponse;
import com.tgfc.tw.entity.repository.StoreRepository;
import com.tgfc.tw.entity.repository.UserRepository;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.UserAllRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class UserAllRecordServiceImp implements UserAllRecordService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;

    @Override
    public Page<UserOrderRecordGroupResponse> getUserAllRecord(UserDailyRecordRequest model) {

        int pageNumber = (model.getPageNumber() >= 1) ? (model.getPageNumber() - 1) : 0;
        Pageable pageable = PageRequest.of(pageNumber, model.getPageSize());

        User user = userRepository.getUserById(ContextHolderHandler.getId());

        Date date1 = model.getStartDate();
        Date date2 = model.getEndDate();

        java.sql.Date startDate = new java.sql.Date(date1.getTime());
        java.sql.Date endDate = new java.sql.Date(date2.getTime()+1 * 24 * 60 * 60 * 1000);
        List<Map<String, Object>> getUserOrderRecordStore = userRepository.getUserOrderRecordStore(user.getId(),startDate,endDate);
        List<Map<String, Object>> getUserOrderRecord =userRepository.getUserOrderRecordDay(user.getId(),startDate,endDate);
        List<UserOrderRecordGroupResponse> userOrderRecordGroupResponseList = new ArrayList<>();
        Page<Map<String, Object>> getUserRecord = userRepository.getUserRecord(user.getId(),startDate,endDate, pageable);



        for (Map userRecord : getUserRecord) {
            List<UserOrderDetailResponse> groupOrderDetailList = new ArrayList<>();
            int groupSum = 0;
            for(Map userOrderRecordStore : getUserOrderRecordStore){
                List <OrderDetailResponse> foodDetail = new ArrayList<>();
                int storeSum = 0;
                for(Map userOrderRecord:getUserOrderRecord){
                    if((int)userOrderRecordStore.get("storeId") == (int)userOrderRecord.get("storeId") && (int)userOrderRecordStore.get("gId") == (int)userOrderRecord.get("gId")){
                        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
                                (String) userOrderRecord.get("optionName"),
                                    (BigInteger) userOrderRecord.get("itemCount"),
                                    (int)userOrderRecord.get("productPrice"),
                                    (int)userOrderRecord.get("optionPrice"),
                                    (String)userOrderRecord.get("productName")
                        );
                        storeSum = storeSum + orderDetailResponse.getTotalPrice();
                        foodDetail.add(orderDetailResponse);
                    }
                }
                if((int) userRecord.get("groupId") == (int) userOrderRecordStore.get("gId")){

                            UserOrderDetailResponse userOrderDetailResponse = new UserOrderDetailResponse(
                                    (String)userOrderRecordStore.get("storeName"),
                                    foodDetail,
                                    storeSum
                            );
                                groupSum = groupSum+ userOrderDetailResponse.getStoreSum();
                                groupOrderDetailList.add(userOrderDetailResponse);
                        }


                    }

            UserOrderRecordGroupResponse userOrderRecordGroupResponse = new UserOrderRecordGroupResponse(
                    (Timestamp) userRecord.get("openDate"),
                    (Timestamp) userRecord.get("startTime"),
                    (Timestamp) userRecord.get("endTime"),
                    (boolean) userRecord.get("isLocked"),
                    (String) userRecord.get("groupName"),
                    (int) userRecord.get("groupId"),
                    groupOrderDetailList,
                    groupSum
                             );
            userOrderRecordGroupResponseList.add(userOrderRecordGroupResponse);
                }

        Page<UserOrderRecordGroupResponse> userAllOrderRecordResponsePage = new PageImpl<>(userOrderRecordGroupResponseList, pageable, getUserRecord.getTotalElements());

            return userAllOrderRecordResponsePage;
    }
}
