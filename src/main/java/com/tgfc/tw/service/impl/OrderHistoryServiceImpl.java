package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.*;
import com.tgfc.tw.entity.model.response.*;
import com.tgfc.tw.entity.model.response.orderHistory.*;
import com.tgfc.tw.entity.model.response.team.TeamResponse;
import com.tgfc.tw.entity.model.response.user.*;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.GroupService;
import com.tgfc.tw.service.OrderHistoryService;
import com.tgfc.tw.service.StorePictureService;
import com.tgfc.tw.service.exception.FloorIdNullException;
import com.tgfc.tw.service.exception.TeamIdNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    @Autowired
    StoreReviewRepository storeReviewRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    OrderHistoryRepository orderHistoryRepository;
    @Autowired
    OrderPassRepository orderPassRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserPayRepository userPayRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    StorePictureService storePictureService;
    @Autowired
    GroupService groupService;
    @Autowired
    FileServiceImpl fileServiceImpl;
    @Autowired
    FloorRepository floorRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    StoreInfoServiceImpl storeInfoServiceImpl;

    @Autowired
    StorePictureRepository storePictureRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private EntityManagerFactory emf;

    private Logger logger = LoggerFactory.getLogger(OrderHistoryService.class);

    @Override
    @Deprecated
    public OrderHistoryGroupResponse getGroup(int groupId) throws FloorIdNullException {
        List<Integer> teamIdList = ContextHolderHandler.getTeamIdList();
        if (teamIdList == null)
            throw new FloorIdNullException();

        List<Map<String, Object>> groupItem = groupRepository.getGroupById(groupId);

        OrderHistoryGroupResponse groupResponse = new OrderHistoryGroupResponse(
                (int) groupItem.get(0).get("groupId"),
                (String) groupItem.get(0).get("groupName"),
                (Timestamp) groupItem.get(0).get("startTime"),
                (Timestamp) groupItem.get(0).get("endTime"),
                (boolean) groupItem.get(0).get("isLocked"));
        Set<Integer> storeIdList = groupItem.stream().map(r -> (int) r.get("storeId")).collect(Collectors.toSet());
        Set<Integer> groupUserList = groupItem.stream().map(r -> (int) r.get("userId")).collect(Collectors.toSet());

        List<OrderHistoryStoreResponse> storeList = new ArrayList<>();
        List<StoreResponse> storeResponses = new ArrayList<>();
        List<TagResponse> tagResponseList = new ArrayList<>();
        for (Integer storeId : storeIdList) {
            List<Map<String, Object>> storeItem = storeRepository.getStoreById(storeId);
            Set<OrderHistoryStorePicResponse> pictureSet = storeItem.stream().filter(q -> q.get("storePictureId") != null)
                    .map(r -> new OrderHistoryStorePicResponse((int) r.get("storePictureId"), (String) r.get("storePictureName"), (String) r.get("storePictureUrl")))
                    .collect(Collectors.toSet());
            List<OrderHistoryStorePicResponse> storePictureList = new ArrayList<>(pictureSet);
            Set<TagResponse> tagSet = storeItem.stream().filter(q -> q.get("tagId") != null)
                    .map(r -> new TagResponse((int) r.get("tagId"), (String) r.get("tagName")))
                    .collect(Collectors.toSet());
            Map<Integer, String> tagList = new HashMap<>();
            tagSet.forEach(r -> tagList.put(r.getId(), r.getName()));
            OrderHistoryStoreResponse store = new OrderHistoryStoreResponse((int) storeItem.get(0).get("storeId"), (String) storeItem.get(0).get("storeName"),
                    (String) storeItem.get(0).get("storeTel"), (String) storeItem.get(0).get("storeRemark")
                    , tagList, storePictureList);
            storeList.add(store);
            StoreResponse storeOne = new StoreResponse((int) storeItem.get(0).get("storeId"), (String) storeItem.get(0).get("storeName"), tagList);
            storeResponses.add(storeOne);
            if (tagResponseList.size() == 0) {
                tagResponseList.addAll(tagSet);
            } else {
                List<TagResponse> tagResponses = tagSet.stream().filter(r -> !tagResponseList.contains(r)).collect(Collectors.toList());
                tagResponseList.addAll(tagResponses);
            }
        }
        groupResponse.setStore(storeList);
        groupResponse.setTagList(tagResponseList);
        Map<Integer, GroupUserStatusResponse> userStatus = groupService.getUserStatus(groupId, storeResponses, ContextHolderHandler.getId());
        Optional<Integer> manager = groupUserList.stream().filter(r -> r == ContextHolderHandler.getId()).findFirst();
        List<Role> roles = userRepository.getUserById(ContextHolderHandler.getId()).getRoleList();
        if (manager.isPresent() && (roles != null) && (roles.size() != 0)) {
            groupResponse.setManager(true);
        }

        if (((Timestamp) groupItem.get(0).get("endTime")).before(new Timestamp(new Date().getTime())))
            groupResponse.setLocked(true);

        groupResponse.setUserStatus(userStatus);
        return groupResponse;
    }

    @Override
    public OrderHistoryGroupResponseV2_1 getGroupV2_1(int groupId) {
        List<Integer> teamIdList = ContextHolderHandler.getTeamIdList();
        if (teamIdList == null)
            throw new TeamIdNullException();

        List<Map<String, Object>> groupItem = groupRepository.getGroupById(groupId);
        DecimalFormat df = new DecimalFormat("##.0");
        OrderHistoryGroupResponseV2_1 groupResponse = new OrderHistoryGroupResponseV2_1(
                (int) groupItem.get(0).get("groupId"),
                (String) groupItem.get(0).get("groupName"),
                (Timestamp) groupItem.get(0).get("startTime"),
                (Timestamp) groupItem.get(0).get("endTime"),
                (boolean) groupItem.get(0).get("isLocked"));

        Set<Integer> storeIdList = groupItem.stream().map(r -> (int) r.get("storeId")).collect(Collectors.toSet());
        Set<Integer> groupUserList = groupItem.stream().map(r -> (int) r.get("userId")).collect(Collectors.toSet());

        List<OrderHistoryStoreResponseV2_1> storeList = new ArrayList<>();
        List<StoreResponse> storeResponses = new ArrayList<>();
        List<TagResponse> tagResponseList = new ArrayList<>();
        for (Integer storeId : storeIdList) {
            List<Map<String, Object>> storeItem = storeRepository.getStoreByIdV2_1(storeId);
            List<OrderHistoryStorePicResponse> storePic = storePictureRepository.getStorePic(storeId);
            List<Map<String, Object>> reviewItem = storeReviewRepository.getSingleReview(storeId);

            List<OrderHistoryStorePicResponse> storePictureList = new ArrayList<>(storePic);
            Set<TagResponse> tagSet = storeItem.stream().filter(q -> q.get("tagId") != null)
                    .map(r -> new TagResponse((int) r.get("tagId"), (String) r.get("tagName")))
                    .collect(Collectors.toSet());
            Map<Integer, String> tagList = new HashMap<>();
            tagSet.forEach(r -> tagList.put(r.getId(), r.getName()));

            BigInteger totalUsers = (BigInteger) reviewItem.get(0).get("totalUsers");
            if((reviewItem.get(0).get("review")) != null) {
                double rank = Double.parseDouble(df.format((((BigDecimal) reviewItem.get(0).get("review")).doubleValue()) / totalUsers.intValue()));
                OrderHistoryStoreResponseV2_1 store = new OrderHistoryStoreResponseV2_1((int) storeItem.get(0).get("storeId"), (String) storeItem.get(0).get("storeName"),
                        (String) storeItem.get(0).get("storeTel"), (String) storeItem.get(0).get("storeRemark"),
                        totalUsers.intValue(), rank, tagList, storePictureList);
                storeList.add(store);
            }else{
                double rank = 0;
                OrderHistoryStoreResponseV2_1 store = new OrderHistoryStoreResponseV2_1((int) storeItem.get(0).get("storeId"), (String) storeItem.get(0).get("storeName"),
                        (String) storeItem.get(0).get("storeTel"), (String) storeItem.get(0).get("storeRemark"),
                        totalUsers.intValue(), rank, tagList, storePictureList);
                storeList.add(store);
            }
            StoreResponse storeOne = new StoreResponse((int) storeItem.get(0).get("storeId"), (String) storeItem.get(0).get("storeName"), tagList);
            storeResponses.add(storeOne);
            if (tagResponseList.size() == 0) {
                tagResponseList.addAll(tagSet);
            } else {
                List<TagResponse> tagResponses = tagSet.stream().filter(r -> !tagResponseList.contains(r)).collect(Collectors.toList());
                tagResponseList.addAll(tagResponses);
            }
        }
        groupResponse.setStore(storeList);
        groupResponse.setTagList(tagResponseList);
        Map<Integer, GroupUserStatusResponse> userStatus = groupService.getUserStatus(groupId, storeResponses, ContextHolderHandler.getId());
        Optional<Integer> manager = groupUserList.stream().filter(r -> r == ContextHolderHandler.getId()).findFirst();
        List<Role> roles = userRepository.getUserById(ContextHolderHandler.getId()).getRoleList();
        if (manager.isPresent() && (roles != null) && (roles.size() != 0)) {
            groupResponse.setManager(true);
        }

        if (((Timestamp) groupItem.get(0).get("endTime")).before(new Timestamp(new Date().getTime())))
            groupResponse.setLocked(true);

        groupResponse.setUserStatus(userStatus);
        return groupResponse;
    }

    @Override
    public List<OrderHistoryCountOrderResponseV2_1> getGroupByOrderV2_1(int groupId, int storeId, List<Integer> teamIdLIst) {

        List<Map<String, Object>> items = orderRepository.getOrderHistoryV2_1(groupId, storeId, teamIdLIst);
        List<OrderHistoryByGroupResponseV2_1> orderResult = items.stream().
                map(r -> new OrderHistoryByGroupResponseV2_1((int) r.get("productId"), (String) r.get("productName"),
                        (int) r.get("productPrice"), (int) r.get("optionId"),
                        (String) r.get("optionName"), (int) r.get("optionPrice"),
                        (int) r.get("orderId"), ((BigInteger) r.get("count")).intValue()))
                .collect(Collectors.toList());
        List<OrderHistoryCountByOrderResponseV2_1> orders = new ArrayList<>();
        Set<Integer> orderIds = orderResult.stream().map(r -> r.getProductId()).collect(Collectors.toSet());

        for (Integer orderId : orderIds) {
            OrderHistoryCountByOrderResponseV2_1 order = new OrderHistoryCountByOrderResponseV2_1();
            order.setId(orderId);
            Set<Integer> optionIdList = orderResult.stream().filter(r -> r.getProductId() == orderId).map(q -> q.getOptionId()).collect(Collectors.toSet());
            ArrayList<Integer> optionIds = new ArrayList<>(optionIdList);
            Collections.sort(optionIds);
            List<OptionCountResponseV2_1> options = new ArrayList<>();
            int totalPrice = 0;
            int totalCount = 0;
            for (Integer optionId : optionIds) {

                for (OrderHistoryByGroupResponseV2_1 history : orderResult) {
                    if (history.getProductId() == orderId && history.getOptionId() == optionId) {
                        OptionCountResponseV2_1 option = new OptionCountResponseV2_1(
                                history.getOptionId(), history.getOptionName(),
                                history.getOptionPrice(), history.getCount());
                        options.add(option);
                        order.setName(history.getProductName());
                        order.setPrice(history.getProductPrice());
                        totalPrice += (history.getOptionPrice() + history.getProductPrice()) * history.getCount();
                        totalCount += history.getCount();
                    }
                }

            }
            order.setCount(totalCount);
            order.setTotalPrice(totalPrice);
            order.setContent(options);
            orders.add(order);
        }

        StoreGetOneResponse store = storeInfoServiceImpl.getStoreById(storeId);
        List<OrderHistoryCountOrderResponseV2_1> responseList = new ArrayList<>();
        OrderHistoryCountOrderResponseV2_1 response = new OrderHistoryCountOrderResponseV2_1();
        response.setStore(store);
        response.setCountByOrder(orders);
        responseList.add(response);
        return responseList;
    }


    public List<OrderHistoryCountOrderResponse> getGroupByOrder(int groupId, int storeId, Integer floorId) {

        List<Map<String, Object>> items = orderRepository.getOrderHistoryBy(groupId, storeId, floorId);
        List<OrderHistoryByGroupResponse> orderResult = items.stream().
                map(r -> new OrderHistoryByGroupResponse((int) r.get("productId"), (String) r.get("productName"),
                        (int) r.get("productPrice"), (int) r.get("optionId"),
                        (String) r.get("optionName"), (int) r.get("optionPrice"),
                        (int) r.get("orderId"), ((BigInteger) r.get("count")).intValue()))
                .collect(Collectors.toList());
        List<OrderHistoryCountByOrderResponse> orders = new ArrayList<>();
        Set<Integer> orderIds = orderResult.stream().map(r -> r.getProductId()).collect(Collectors.toSet());

        for (Integer orderId : orderIds) {
            OrderHistoryCountByOrderResponse order = new OrderHistoryCountByOrderResponse();
            order.setId(orderId);
            Set<Integer> optionIdList = orderResult.stream().filter(r -> r.getProductId() == orderId).map(q -> q.getOptionId()).collect(Collectors.toSet());
            ArrayList<Integer> optionIds = new ArrayList<>(optionIdList);
            Collections.sort(optionIds);
            List<OptionCountResponse> options = new ArrayList<>();
            int totalPrice = 0;
            int totalCount = 0;
            for (Integer optionId : optionIds) {

                for (OrderHistoryByGroupResponse history : orderResult) {
                    if (history.getProductId() == orderId && history.getOptionId() == optionId) {
                        OptionCountResponse option = new OptionCountResponse(
                                history.getOptionId(), history.getOptionName(),
                                history.getOptionPrice(), history.getCount());
                        options.add(option);
                        order.setName(history.getProductName());
                        order.setPrice(history.getProductPrice());
                        totalPrice += (history.getOptionPrice() + history.getProductPrice()) * history.getCount();
                        totalCount += history.getCount();
                    }
                }

            }
            order.setCount(totalCount);
            order.setTotalPrice(totalPrice);
            order.setContent(options);
            orders.add(order);
        }

        StoreGetOneResponse store = storeInfoServiceImpl.getStoreById(storeId);
        List<OrderHistoryCountOrderResponse> responseList = new ArrayList<>();
        OrderHistoryCountOrderResponse response = new OrderHistoryCountOrderResponse();
        response.setStore(store);
        response.setCountByOrder(orders);
        responseList.add(response);
        return responseList;
    }

    public List<OrderHistoryCountUserResponse> getGroupByUser(int groupId, Integer storeId, int floorId, Integer userId) {
//        List<StoreResponse> storeNameList = new ArrayList<>();
//        if (storeId == null) {
//            storeNameList = groupRepository.getStoreNameList(groupId);
//        } else {
//            StoreResponse storeResponse = new StoreResponse();
//            storeResponse.setId(storeId);
//            storeNameList.add(storeResponse);
//        }
//        List<User> userList = new ArrayList<>();
//        if (userId == null) {
//            userList = userRepository.findUserByFloor(floorId);
//        } else {
//            User user = userRepository.getOne(userId);
//            userList.add(user);
//        }
//        List<OrderHistoryCountByUserResponse> orderHistoryByGroupList = orderRepository.getUserCount(groupId, storeId, floorId, userId);
//        List<OrderPass> orderPassList = orderPassRepository.findOrderPassByGroup(groupId);
//        List<OrderHistoryUserCountResponse> userCountResponseList = new ArrayList<>();
//        for (User user : userList) {
//            List<OrderHistoryCountByUserResponse> orderHistoryByUserList = orderHistoryByGroupList.stream().filter(r -> r.getUserId() == user.getId()).collect(Collectors.toList());
//            List<OrderStatusResponse> orderStatusResponseList = new ArrayList<>();
//            for (StoreResponse store : storeNameList) {
//                List<OrderHistoryCountByUserResponse> orderItems = orderHistoryByUserList.stream().filter(r -> r.getStoreId() == store.getId()).collect(Collectors.toList());
//                OrderStatusResponse orderStatusResponse = new OrderStatusResponse(user.getId(), store.getId());
//                Optional<OrderPass> orderPass = orderPassList.stream().filter(r -> r.getUser().getId() == user.getId()).filter(r -> r.getStore().getId() == store.getId()).findFirst();
//                if (orderItems.size() > 0) {
//                    orderStatusResponse.setUserStatus(1);
//                } else if (orderPass.isPresent()) {
//                    orderStatusResponse.setUserStatus(-1);
//                } else {
//                    orderStatusResponse.setUserStatus(0);
//                }
//                orderStatusResponseList.add(orderStatusResponse);
//            }
//            long totalPrice = orderHistoryByUserList.stream().mapToLong(r -> r.getTotalPrice()).sum();
//            OrderHistoryUserCountResponse userCountResponse = new OrderHistoryUserCountResponse(user.getEnglishName(), totalPrice, orderHistoryByUserList, orderStatusResponseList);
//            userCountResponseList.add(userCountResponse);
//        }
//
//        if (storeId != null) {
//            userCountResponseList = userCountResponseList.stream()
//                    .sorted(Comparator.comparing((OrderHistoryUserCountResponse r) -> r.getSortCount(storeId)).reversed())
//                    .collect(Collectors.toList());
//        } else {
//            userCountResponseList = userCountResponseList.stream()
//                    .sorted(Comparator.comparing((OrderHistoryUserCountResponse r) -> r.getCountByUser().size()).reversed())
//                    .collect(Collectors.toList());
//        }
//
//        List<OrderHistoryCountUserResponse> responseList = new ArrayList<>();
//        OrderHistoryCountUserResponse response = new OrderHistoryCountUserResponse();
//        response.setUserCountByGroup(userCountResponseList);
//        responseList.add(response);
        List<OrderHistoryCountUserResponse> responseList = new ArrayList<>();
        return responseList;
    }

    @Override
    @Deprecated
    public List<OrderHistoryCountUserResponse> getGroupByOrderUser(int groupId, List<Integer> storeId, Integer floorId, Integer userId) {

        if (storeId == null || storeId.size() == 0) {
            storeId = new ArrayList<>();
            List<Map<String, Object>> list = storeRepository.getGroupStoreByGroupId(groupId);
            for (Map<String, Object> o : list) {
                storeId.add((Integer) o.get("sid"));
            }
        }

        List<OrderHistoryCountUserResponse> storeByGroup = new ArrayList<>();
        List<Map<String, Object>> storeDataList = storeRepository.getGroupStoreDataByGroupId(groupId, storeId);
        List<Map<String, Object>> storePicList = storeRepository.getGroupStorePicByGroupId(groupId);
        List<Map<String, Object>> storeTagList = storeRepository.getGroupStoreTagByGroupId(groupId);

        List<StoreGetOrderDetailResponse> storeList = new ArrayList<>();
        OrderHistoryCountUserResponse orderHistoryCountUserResponse = new OrderHistoryCountUserResponse();

        storeDataList.stream().forEach(r -> {
            List<StorePictureResponse> picList = storePicList.stream().filter(p -> p.get("sid").equals(r.get("sid")))
                    .map(q -> new StorePictureResponse(q))
                    .collect(Collectors.toList());
            List<TagResponse> tagList = storeTagList.stream().filter(p -> p.get("sid").equals(r.get("sid")))
                    .map(q -> new TagResponse(q))
                    .collect(Collectors.toList());
            StoreGetOrderDetailResponse storeGetOrderDetailResponse = new StoreGetOrderDetailResponse();
            storeGetOrderDetailResponse.setId((int) r.get("sid"));
            storeGetOrderDetailResponse.setStoreName(String.valueOf(r.get("storeName")));
            storeGetOrderDetailResponse.setStoreTel(String.valueOf(r.get("storeTel")));
            storeGetOrderDetailResponse.setStoreAddress(String.valueOf(r.get("storeAddress")));
            storeGetOrderDetailResponse.setRemark(String.valueOf(r.get("remark")));
            storeGetOrderDetailResponse.setPictures(picList);
            storeGetOrderDetailResponse.setTag(tagList);

            storeList.add(storeGetOrderDetailResponse);
            orderHistoryCountUserResponse.setStores(storeList);
        });
        storeByGroup.add(orderHistoryCountUserResponse);

        List<OrderHistoryCountUserResponse> stores = new ArrayList<>();
        if (storeId == null) {
            stores = storeByGroup;
        } else {
            for (OrderHistoryCountUserResponse item : storeByGroup) {
                for (StoreGetOrderDetailResponse s : item.getStores()) {
                    if (storeId.contains(s.getId())) {
                        stores.add(item);
                    }
                }
            }
        }

        List<OrderHistoryCountByUserResponse> userCount = orderRepository.getUserCount(groupId, storeId, floorId, userId);
        //List<UserPay> userPayByGroup = userPayRepository.getByGroupId(groupId);
        List<OrderHistoryUserCountResponse> userCountResponseList = new ArrayList<>();
        List<Integer> userList = userCount.stream().map(r -> r.getUserId()).collect(Collectors.toList());
        Set<Integer> userSet = new HashSet<>(userList);
        ArrayList<Integer> userSortList = new ArrayList<>(userSet);
        Collections.sort(userSortList);
        for (Integer userNumber : userSortList) {
            List<OrderHistoryCountByUserResponse> orderItems = userCount.stream().filter(r -> r.getUserId() == userNumber).collect(Collectors.toList());
            long totalPrice = orderItems.stream().mapToLong(r -> r.getTotalPrice()).sum();
//            List<UserPayResponse> payList = new ArrayList<>();
//            List<UserPay> pays = userPayByGroup.stream().filter(r -> r.getUser().getId() == userNumber).collect(Collectors.toList());
            int totalPay = 0;
            //todo
//            for(StoreGetOrderDetailResponse store : stores.get(0).getStores()){
//                    Optional<UserPay> payForStore = pays.stream().filter(q -> q.getStore().getId() == store.getId()).findFirst();
//                    long countByStore = orderItems.stream().filter(q -> q.getStoreId() == store.getId()).mapToLong(r -> r.getTotalPrice()).sum();
//                    if (payForStore.isPresent()){
//                        UserPay userPay = payForStore.get();
//                        payList.add(new UserPayResponse(userPay));
//                        totalPay += userPay.getPay();
//                    } else {
//                        payList.add(new UserPayResponse(store.getId(), false, 0, (int) countByStore));
//                    }
//            }

            OrderHistoryUserCountResponse userCountResponse = new OrderHistoryUserCountResponse(
                    orderItems.get(0).getUserId(), orderItems.get(0).getUserName(), totalPrice, totalPay, orderItems, null);
            userCountResponseList.add(userCountResponse);
        }
        List<OrderHistoryCountUserResponse> responseList = new ArrayList<>();
        OrderHistoryCountUserResponse response = new OrderHistoryCountUserResponse();

        stores.stream().forEach(s -> response.setStores(s.getStores()));
        response.setUserCountByGroup(userCountResponseList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<OrderHistoryCountUserResponseV2_1> getGroupByOrderUserV2_1(int groupId, List<Integer> storeId, List<Integer> teamIdList, Integer userId) {

       teamIdList =  teamIdList == null || (teamIdList.size() == 0) ? null : teamIdList;
        if (storeId == null || storeId.size() == 0) {
            storeId = new ArrayList<>();
            List<Map<String, Object>> list = storeRepository.getGroupStoreByGroupId(groupId);
            for (Map<String, Object> o : list) {
                storeId.add((Integer) o.get("sid"));
            }
        }

        List<OrderHistoryCountUserResponseV2_1> storeByGroup = new ArrayList<>();
        List<Map<String, Object>> storeDataList = storeRepository.getGroupStoreDataByGroupId(groupId, storeId);
        List<Map<String, Object>> storePicList = storeRepository.getGroupStorePicByGroupId(groupId);
        List<Map<String, Object>> storeTagList = storeRepository.getGroupStoreTagByGroupId(groupId);

        List<StoreGetOrderDetailResponseV2_1> storeList = new ArrayList<>();
        OrderHistoryCountUserResponseV2_1 orderHistoryCountUserResponseV2_1 = new OrderHistoryCountUserResponseV2_1();

        storeDataList.stream().forEach(r -> {
            List<StorePictureResponse> picList = storePicList.stream().filter(p -> p.get("sid").equals(r.get("sid")))
                    .map(q -> new StorePictureResponse(q))
                    .collect(Collectors.toList());
            List<TagResponse> tagList = storeTagList.stream().filter(p -> p.get("sid").equals(r.get("sid")))
                    .map(q -> new TagResponse(q))
                    .collect(Collectors.toList());
            StoreGetOrderDetailResponseV2_1 storeGetOrderDetailResponseV2_1 = StoreGetOrderDetailResponseV2_1.valueOf(r);
            storeGetOrderDetailResponseV2_1.setPictures(picList);
            storeGetOrderDetailResponseV2_1.setTag(tagList);

            storeList.add(storeGetOrderDetailResponseV2_1);
            orderHistoryCountUserResponseV2_1.setStores(storeList);
        });
        storeByGroup.add(orderHistoryCountUserResponseV2_1);

        List<OrderHistoryCountUserResponseV2_1> stores = new ArrayList<>();
        if (storeId == null) {
            stores = storeByGroup;
        } else {
            for (OrderHistoryCountUserResponseV2_1 item : storeByGroup) {
                for (StoreGetOrderDetailResponseV2_1 s : item.getStores()) {
                    if (storeId.contains(s.getId())) {
                        stores.add(item);
                    }
                }
            }
        }

        List<OrderHistoryCountByUserResponse> userCount = orderRepository.getUserCountV2_1(groupId, storeId, teamIdList, userId);
        List<OrderHistoryUserCountResponseV2_1> userCountResponseList = new ArrayList<>();
        List<Integer> userList = userCount.stream().map(r -> r.getUserId()).collect(Collectors.toList());
        Set<Integer> userSet = new HashSet<>(userList);
        ArrayList<Integer> userSortList = new ArrayList<>(userSet);
        Collections.sort(userSortList);
        for (Integer userNumber : userSortList) {
            List<OrderHistoryCountByUserResponse> orderItems = userCount.stream().filter(r -> r.getUserId() == userNumber).collect(Collectors.toList());
            long totalPrice = orderItems.stream().mapToLong(r -> r.getTotalPrice()).sum();

            int totalPay = 0;

            OrderHistoryUserCountResponseV2_1 userCountResponse = new OrderHistoryUserCountResponseV2_1(
                    orderItems.get(0).getUserId(), orderItems.get(0).getUserName(), totalPrice, totalPay, orderItems);
            userCountResponseList.add(userCountResponse);
        }
        List<OrderHistoryCountUserResponseV2_1> responseList = new ArrayList<>();
        OrderHistoryCountUserResponseV2_1 response = new OrderHistoryCountUserResponseV2_1();

        stores.stream().forEach(s -> response.setStores(s.getStores()));
        response.setUserCountByGroup(userCountResponseList);
        responseList.add(response);
        return responseList;
    }

    @Deprecated
    @Override
    @Transactional
    public void addOrderItem(int optionId, int groupId) throws Exception {

        logger.info("OrderHistoryService_addOrderItem optionId:{}, groupId:{}", optionId, groupId);

        int userIdServer = ContextHolderHandler.getId();

        //先確認是否已有order_history
        Optional<Order> oldItem = orderHistoryRepository.findOrderHistory(optionId, groupId);
        Optional<User> user = userRepository.findById(userIdServer);
        Group group = groupRepository.getById(groupId);

        Optional<Option> option = optionRepository.findById(optionId);


        if (!user.isPresent())
            throw new Exception("新增失敗，會員資料庫尚未建檔");
        if (!option.isPresent())
            throw new Exception("新增失敗，餐點說明資料庫尚未建檔");
        if (!group.isReleaseTime())
            throw new RuntimeException("此團尚未開始，或是已經結束");
        if (group.isLocked() && !group.getUserList().contains(user.get()))
            throw new RuntimeException("已結單，只有管理員可以點餐");

        Floor userFloor = user.get().getFloor();
        List<Floor> groupFloorList = group.getFloorList();
        if (!groupFloorList.contains(userFloor))
            throw new Exception("不屬於這個團設定的樓層，所以無法點餐，有需要請洽管理員");

        //如果是尚未點過的餐點選項
        if (!oldItem.isPresent()) {
            //建新的OrderHistory
            Order newOrder = new Order();
            User newUser = userRepository.getById(userIdServer).get();
            List<User> users = new ArrayList<>();
            users.add(newUser);
            newOrder.setDate(new Timestamp(new Date().getTime()));
            newOrder.setOption(option.get());
            newOrder.setUserList(users);
            newOrder.setGroup(group);
            newOrder.setStore(storeRepository.getStoreByOptionId(optionId));
            orderHistoryRepository.save(newOrder);
        }
        //已經點過的餐點選項
        else {
            Optional<User> userOptional = userRepository.getById(userIdServer);
            oldItem.get().getUserList().add(userOptional.get());
            orderHistoryRepository.save(oldItem.get());
        }

    }

    @Override
    @Transactional
    public void delOrderItem(int orderId, int groupId) throws Exception {

        logger.info("OrderHistoryService_delOrderItem orderId:{}, groupId:{}", orderId, groupId);

        int userIdServer = ContextHolderHandler.getId();

        Optional<User> user = userRepository.findById(userIdServer);
        Group group = groupRepository.getById(groupId);

        Floor userFloor = user.get().getFloor();
        List<Floor> groupFloorList = group.getFloorList();
        if (!groupFloorList.contains(userFloor))
            throw new Exception("不屬於這個團設定的樓層，所以無法點餐，有需要請洽管理員");
        if (!group.isReleaseTime())
            throw new RuntimeException("此團尚未開始，或是已經結束");
        if (group.isLocked() && !group.getUserList().contains(user.get()))
            throw new RuntimeException("已結單，只有管理員可以點餐");

        deleteOrder(orderId, groupId, userIdServer);
    }

    private void deleteOrder(int orderId, int groupId, int userId) throws Exception {
        List<Order> orderList = orderHistoryRepository.getDelOrderHistory(orderId, groupId, userId);
        if (orderList == null || orderList.size() == 0) {
            throw new Exception("無點餐資料可刪除");
        }
        orderHistoryRepository.delOrderHistory(orderId, groupId, userId);
    }


    @Transactional
    public void managerAddOrder(int optionId, int groupId, int userId) throws Exception {

        logger.info("OrderHistoryService_managerAddOrder orderId:{}, groupId:{}, userId:{}", optionId, groupId, userId);

        int userIdServer = ContextHolderHandler.getId();
        Group group = groupRepository.getById(groupId);
        List<Integer> groupUser = group.getUserList().stream().map(r -> r.getId()).collect(Collectors.toList());
        if (!groupUser.contains(userIdServer)) {
            throw new Exception("本用戶沒有權限可以加點");
        }

        //先確認是否已有order_history
        Optional<Order> oldItem = orderHistoryRepository.findOrderHistory(optionId, groupId);
        Optional<User> user = userRepository.findById(userId);
        Optional<Option> option = optionRepository.findById(optionId);

        if (user.isPresent() == false)
            throw new Exception("加點失敗，會員資料庫尚未建檔");
        if (option.isPresent() == false)
            throw new Exception("加點失敗，餐點資料庫尚未建檔");

        //如果是尚未點過的餐點選項
        if (!oldItem.isPresent()) {
            //建新的OrderHistory
            Order newOrder = new Order();
            User newUser = userRepository.getById(userId).get();
            List<User> users = new ArrayList<>();
            users.add(newUser);
            newOrder.setDate(new Timestamp(new Date().getTime()));
            newOrder.setOption(option.get());
            newOrder.setUserList(users);
            newOrder.setGroup(group);

            newOrder.setStore(storeRepository.getStoreByOptionId(optionId));
            orderHistoryRepository.save(newOrder);

        } else {//已經點過的餐點選項
            Optional<User> userOptional = userRepository.getById(userId);
            oldItem.get().getUserList().add(userOptional.get());
            orderHistoryRepository.save(oldItem.get());
        }
    }


    @Transactional
    public void managerDeleteOrder(Integer orderId, int groupId, int userId) throws Exception {
        Group group = groupRepository.getById(groupId);
        Optional<User> manager = group.getUserList().stream().filter(r -> r.getId() == ContextHolderHandler.getId()).findFirst();
        if (!manager.isPresent()) {
            throw new Exception("本用戶沒有權限可以刪除");
        }

        deleteOrder(orderId, groupId, userId);
    }

    @Override
    @Transactional
    public void changeOrderPass(int groupId, int storeId) throws Exception {

        logger.info("OrderHistoryService_changeOrderPass groupId:{}, storeId:{}", groupId, storeId);

        int userIdServer = ContextHolderHandler.getId();
        Optional<OrderPass> orderPassItem = orderPassRepository.orderPassExist(groupId, storeId, userIdServer);
        Optional<User> user = userRepository.findById(userIdServer);
        Optional<Store> store = storeRepository.findById(storeId);
        Group group = groupRepository.getById(groupId);

        if (!user.isPresent())
            throw new Exception("新增失敗，會員資料庫尚未建檔");
        if (!store.isPresent())
            throw new Exception("新增失敗，店家資料庫尚未建檔");
        if (!group.isReleaseTime())
            throw new RuntimeException("此團尚未開始，或已經結束");
        if (group.isLocked() && !group.getUserList().contains(user.get()))
            throw new RuntimeException("已結單，只有管理員可以點餐");
        if (orderPassRepository.userAddItems(groupId, storeId).contains(userIdServer))
            throw new Exception("您已點餐，請先取消訂餐在PASS");

        if (!orderPassItem.isPresent()) {
            OrderPass orderPass = new OrderPass();
            User newUser = user.get();
            Store storePass = store.get();

            orderPass.setUser(newUser);
            orderPass.setStore(storePass);
            orderPass.setGroup(group);
            orderPassRepository.save(orderPass);
        } else {
            orderPassRepository.removeOrderPass(orderPassItem.get().getId());
        }
    }

    public List<UserListResponse> getOrderUserList(int productId, int groupId) {
        List<UserListResponse> response = orderHistoryRepository.getOrderUserList(productId, groupId);
        return response;
    }

    @Override
    public List<UserListByRemoveOrderResponseV2_1> getOrderUserListV2_1(int orderId, int groupId) {
        List<UserListByRemoveOrderResponseV2_1> response = orderHistoryRepository.getOrderUserListV2_1(orderId, groupId);
        return response;
    }

    //取得團點餐資料(僅餐點項目資料)
    public GroupOrderItemResponse getGroupOrderItem(int groupId, int storeId, String keyword, int pageNumber, int pageSize, Integer orderType, Integer sortType) throws Exception {

        int userIdServer = ContextHolderHandler.getId();

        keyword = keyword == null || keyword.isEmpty() ? "%" : "%" + keyword + "%";
        pageNumber = (pageNumber >= 1) ? (pageNumber - 1) : 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (orderType == null || sortType == null) {
            orderType = 1;
            sortType = 1;
        }
        int sortNumber = sortType * 10 + orderType;

        Page<Map<String, Object>> listMap = orderRepository.getOrderHistoryCountBy(groupId, storeId, userIdServer, keyword, pageable);
        List<Map<String, Object>> userList = orderRepository.getOrderUserByGroupId(groupId, storeId);
        List<OrderHistoryGetCountByOrderResponse> responseList = new ArrayList<>();
        TotalCalcResponse totalCalc = new TotalCalcResponse();
        for (Map m : listMap) {
            List<UserNameOrderOptionResponse> list = new ArrayList<>();
            OrderHistoryGetCountByOrderResponse response = new OrderHistoryGetCountByOrderResponse();
            for (Map u : userList) {
                if (Integer.parseInt(u.get("productId").toString()) == Integer.parseInt(m.get("id").toString())) {
                    UserNameOrderOptionResponse ur = new UserNameOrderOptionResponse
                            (Integer.parseInt(u.get("id").toString()),
                                    u.get("floor").toString(), u.get("englishName").toString(),
                                    Integer.parseInt(u.get("optionId").toString()),
                                    Integer.parseInt(u.get("count").toString()));
                    list.add(ur);
                }
            }
            response.setId(Integer.parseInt(m.get("id").toString()));
            response.setName(m.get("name").toString());
            response.setRemark(m.get("remark") == null ? "" : m.get("remark").toString());
            response.setPrice(Integer.parseInt(m.get("order_price").toString()));
            response.setCount(Long.parseLong(m.get("order_count").toString()));
            response.setOptionAllPrice(Long.parseLong(m.get("option_price").toString()));
            response.setOrderTotalPrice(Long.parseLong(m.get("total").toString()));
            response.setUsers(list);
            responseList.add(response);
        }
        Map<String, Object> totalPriceList = orderRepository.getOrderHistoryTotalPriceBy(groupId, storeId, keyword);
        totalCalc.settOrderCount(((BigInteger) totalPriceList.get("count")).longValue());
        totalCalc.settOrderPrice(totalPriceList.get("total") == null ? 0 : ((BigDecimal) totalPriceList.get("total")).longValue());
        Page<OrderHistoryGetCountByOrderResponse> page = new PageImpl<>(responseList, pageable, listMap.getTotalElements());
        GroupOrderItemResponse result = new GroupOrderItemResponse();
        result.setPage(page);
        result.setTotalCalcResponse(totalCalc);
        return result;
    }

    @Override
    public InitGroupUserResponse getInitDataByUser(int groupId) {
        Group group = groupRepository.getById(groupId);
        List<Store> storeList = group.getStoreList();
        List<Floor> floorsList = group.getFloorList();
        List<StoreResponse> stores = new ArrayList<>();
        List<FloorResponse> floors = new ArrayList<>();
        storeList.forEach(r -> stores.add(new StoreResponse(r)));
        floors = floorsList.size() > 0 ?
                floorsList.stream().map(r -> new FloorResponse(r)).collect(Collectors.toList()) :
                floorRepository.getAllFloor();
        List<UserNameResponse> users = orderHistoryRepository.getOrderUserByGroup(groupId);
        return new InitGroupUserResponse(groupId, users, floors, stores);
    }

    @Override
    public InitGroupUserResponseV2_1 getInitDataByUserV2_1(int groupId) {
        Group group = groupRepository.getById(groupId);
        List<Store> storeList = group.getStoreList();
        List<Team> teamList = group.getTeamList();
        List<StoreResponse> stores = new ArrayList<>();
        List<TeamResponse> teams = new ArrayList<>();
        storeList.forEach(r -> stores.add(new StoreResponse(r)));
        teams = teamList.size() > 0 ?
                teamList.stream().filter(r -> !r.isDeleted()).map(r -> new TeamResponse(r)).collect(Collectors.toList()) :
                teamRepository.getAllNotDeleted();
        List<UserNameResponseV2_1> users = orderHistoryRepository.getOrderUserByGroupV2_1(groupId);
        return new InitGroupUserResponseV2_1(groupId, users, teams, stores);
    }

    @Override
    public InitOrderResponse getOrderInitData(int groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent())
            throw new RuntimeException("error group id");

        Group group = groupOptional.get();
        InitOrderResponse result = new InitOrderResponse();
        List<FloorResponse> floorsList =
                group.getFloorList().size() > 0 ?
                        group.getFloorList().stream().map(r -> new FloorResponse(r)).collect(Collectors.toList()) :
                        floorRepository.getAllFloor();

        result.setStores(group.getStoreList().stream().map(r -> new StoreNameResponse(r.getId(), r.getName())).collect(Collectors.toList()));
        result.setFloors(floorsList);

        return result;
    }

    @Override
    public InitOrderResponseV2_1 getOrderInitDataV2_1(int groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent())
            throw new RuntimeException("error group id");

        Group group = groupOptional.get();
        InitOrderResponseV2_1 result = new InitOrderResponseV2_1();
        List<TeamResponse> teamsList =
                group.getTeamList().size() > 0 ?
                        group.getTeamList().stream().filter(r -> !r.isDeleted()).map(r -> new TeamResponse(r)).collect(Collectors.toList()) :
                        teamRepository.getAllNotDeleted();

        result.setStores(group.getStoreList().stream().map(r -> new StoreNameResponse(r.getId(), r.getName())).collect(Collectors.toList()));
        result.setTeams(teamsList);

        return result;
    }
}
