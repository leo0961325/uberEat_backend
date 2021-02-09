package com.tgfc.tw.service.impl;


import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.po.Group;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.po.UserPay;
import com.tgfc.tw.entity.model.request.AddStoreValueRequest;
import com.tgfc.tw.entity.model.response.TeamResponse;
import com.tgfc.tw.entity.model.response.pay.*;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    UserPayRepository userPayRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreReviewRepository storeReviewRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    TeamRepository teamRepository;

    @Override
    public void addStoreValue(AddStoreValueRequest request) throws Exception {

        User login = checkUser(ContextHolderHandler.getId());
        User user = checkUser(request.getMemberId());
        UserPay userPay = new UserPay();
        if (user.getRoleList().stream().anyMatch(r -> r.getCode().equals(PermissionEnum.ROLE_SUPER_MANAGER.getPermissionName()))) {
            throw new Exception("不能幫admin儲值扣款");
        }
        if (request.isStatus()) {
            if (login.equals(user)) {
                throw new Exception("不能幫自己儲值");
            }
            userPay.setPay(request.getPay());
            userPay.setStatus(true);
            userPay.setPayType(request.getPayType());
        } else {
            userPay.setPay(request.getPay());
            userPay.setStatus(false);
        }
        userPay.setUser(user);
        userPay.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        if (request.getRemark() != null) {
            userPay.setRemark(request.getRemark());
        }
        userPay.setManager(login.getEnglishName());
        userPay.setDebit(true);
        userPayRepository.save(userPay);
    }

    @Override
    public Map getBalance() throws Exception {

        int loginId = ContextHolderHandler.getId();
        Map map = new HashMap<>();
        map.put("balance", userPayRepository.getBalance(loginId));
        return map;
    }

    @Override
    public StoreValueRecordResponse getStoreValueRecordList(int memberId, int pageNumber, int pageSize, boolean sortMode) throws Exception {
        Sort sort;
        if (sortMode) {
            sort = Sort.by(Sort.Direction.ASC, "date");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "date");
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        List<UserPay> userPayList = userPayRepository.getByUserId(memberId, pageable);
        List<Map<String, Object>> payDetailList = userPayRepository.getPayDetailByUserId(memberId);
        List<Map<String, Object>> payStoreList = userPayRepository.getStoreByUserId(memberId);
        List<Map<String, Object>> teamList = teamRepository.getTeamListByUserId(memberId);
        List<TeamResponse> teamResponseList = new ArrayList<>();

        if(teamList.size() != 0){
//            for (Map<String, Object> teamMap : teamList) {
//                TeamResponse teamResponse = new TeamResponse((int) teamMap.get("teamId"), (String) teamMap.get("teamName"));
//                teamResponseList.add(teamResponse);
//        }
            teamList.forEach(r -> teamResponseList.add(new TeamResponse((int)r.get("teamId"),(String) r.get("teamName"))));

        }
        int totalCount = userPayRepository.getTotalCount(memberId);

        List<PayRecordResponse> payRecordList = new ArrayList<>();

        int balance = userPayRepository.getBalance(memberId) == null ? 0 : userPayRepository.getBalance(memberId);
        MemberResponse memberResponse = MemberResponse.valueOf(checkUser(memberId), balance, teamResponseList);





        for (UserPay userPay : userPayList) {
            PayRecordResponse payRecordResponse = PayRecordResponse.valueOf(userPay);
            List<PayOrderResponse> orderList = new ArrayList<>();
            int sum = 0;
            List<Integer> storeId = new ArrayList<>();
            for (Map<String, Object> payStoreMap : payStoreList) {
                List<PayOrderDetailResponse> detailList = new ArrayList<>();
                if (payRecordResponse.getId() == (int) payStoreMap.get("payId") && !storeId.contains((int) payStoreMap.get("storeId"))) {
                    storeId.add((int) payStoreMap.get("storeId"));
                    PayOrderResponse payOrderResponse = new PayOrderResponse();
                    payOrderResponse.setStoreName((String) payStoreMap.get("storeName"));
                    for (Map<String, Object> payDetailMap : payDetailList) {
                        if ((int) payStoreMap.get("storeId") == (int) payDetailMap.get("storeId") && (int) payDetailMap.get("payId") == (int) payStoreMap.get("payId")) {
                            sum = sum + (int) payDetailMap.get("totalPrice");
                            PayOrderDetailResponse payOrderDetailResponse =
                                    new PayOrderDetailResponse((String) payDetailMap.get("productName"),
                                            (int) payDetailMap.get("productCount"),
                                            (int) payDetailMap.get("productPrice"),
                                            (int)payDetailMap.get("optionPrice"),
                                            (String)payDetailMap.get("optionName"));
                            detailList.add(payOrderDetailResponse);
                        }
                    }
                    payRecordResponse.setTotalPrice(sum);
                    payOrderResponse.setOrderDetail(detailList);
                    orderList.add(payOrderResponse);
                }


            }
            payRecordResponse.setOrder(orderList);
            payRecordList.add(payRecordResponse);
        }
        Page<PayRecordResponse> payRecordListPage = null;
        if (payRecordList != null && payRecordList.size() != 0) {
            payRecordListPage = new PageImpl<>(payRecordList, pageable, totalCount);
        }

        StoreValueRecordResponse storeValueRecordResponse = new StoreValueRecordResponse(memberResponse, payRecordListPage);


        return storeValueRecordResponse;
    }


    private User checkUser(int id) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new Exception("查無此用戶");

        }
        return userOptional.get();
    }

    @Override
    public void checkPayments() {

        //Lock ExpiredGroups
        List<Group> expireGroups = groupRepository.getExpireGroups();
        if (expireGroups.size() != 0) {
            expireGroups.forEach(r -> r.setLocked(true));
            groupRepository.saveAll(expireGroups);
        }

        //Debit Status Change
        List<Integer> expireGroupsId = groupRepository.getExpireGroupsId();
        if (expireGroupsId.size() != 0) {
            userPayRepository.changeDebitStatus(expireGroupsId);
        }
    }
}
