package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.*;
import com.tgfc.tw.entity.model.request.OptionAddRequestV2_1;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.OrdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdServiceImpl implements OrdService {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    UserPayRepository userPayRepository;

    @Autowired
    PayDetailRepository payDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    private Logger logger = LoggerFactory.getLogger(OrdServiceImpl.class);

    final String SYSTEM_MANAGER = "N/A";

    final boolean NOT_CHARGE = false;
    final boolean CHARGE = true;

    final boolean ADD_VALUE = true;
    final boolean DEDUCTION = false;

    private UserPay.Builder newUserPay;
    private PayDetail.Builder newPayDetail;


    @Override
    @Transactional
    public void addOrderItem(int optionId, int groupId) {

        logger.info("OrdService_addOrderItem optionId:{}, groupId:{}", optionId, groupId);
        int userIdServer = ContextHolderHandler.getId();

        Group group = groupRepository.getById(groupId);

        Optional<User> userOptional = userRepository.findById(userIdServer);
        if (!userOptional.isPresent())
            throw new IllegalArgumentException("新增失敗，會員資料庫尚未建檔");
        User user = userOptional.get();

        Optional<Option> optionOptional = optionRepository.findById(optionId);
        if (!optionOptional.isPresent())
            throw new IllegalArgumentException("新增失敗，餐點說明資料庫尚未建檔");
        Option option = optionOptional.get();
        Product product = option.getProduct();
        Store store = storeRepository.getStoreByOptionId(optionId);
        int orderPrice = product.getPrice() + option.getPrice();

        if (!haveDeposit(userIdServer, orderPrice))
            throw new IllegalArgumentException("儲值金不足，請加值");

        canOrder(group, user);
        Order order = addOrder(option, group, store, user);

        UserPay userPay = userPayRepository.getByGroupOnGoing(userIdServer, groupId, NOT_CHARGE);

        noChargePay(option, group, store, product, order, user);
    }

    private Order addOrder(Option option, Group group, Store store, User user) {
        Optional<Order> oldItem = orderHistoryRepository.findOrderHistory(option.getId(), group.getId());
        Order order = null;
        //如果是尚未點過的餐點選項
        if (!oldItem.isPresent()) {
            //建新的OrderHistory
            Order newOrder = new Order();
            List<User> users = new ArrayList<>();
            users.add(user);
            newOrder.setDate(new Timestamp(new Date().getTime()));
            newOrder.setOption(option);
            newOrder.setUserList(users);
            newOrder.setGroup(group);
            newOrder.setStore(store);
            order = orderHistoryRepository.save(newOrder);
        }
        //已經點過的餐點選項
        else {
            oldItem.get().getUserList().add(user);
            order = orderHistoryRepository.save(oldItem.get());
        }
        return order;
    }

    void noChargePay(Option option, Group group, Store store, Product product, Order order, User user) {
        UserPay userPay = userPayRepository.getByGroupOnGoing(user.getId(), group.getId(), NOT_CHARGE);
        int orderPrice = product.getPrice() + option.getPrice();
        if (userPay == null) {
            UserPay saveUserPay = new UserPay.Builder()
                    .status(DEDUCTION)
                    .payType(0)
                    .date(LocalDateTime.now())
                    .manager(SYSTEM_MANAGER)
                    .debit(NOT_CHARGE)
                    .user(user)
                    .build();
            PayDetail savePayDetail = new PayDetail.Builder()
                    .groupId(group.getId())
                    .groupName(group.getName())
                    .storeId(store.getId())
                    .storeName(store.getName())
                    .productId(product.getId())
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .optionId(option.getId())
                    .optionName(option.getName())
                    .optionPrice(option.getPrice())
                    .count(1)
                    .totalPrice(orderPrice)
                    .orderId(order.getId())
                    .userPay(saveUserPay)
                    .build();
            List<PayDetail> payDetailList = new ArrayList<>();
            payDetailList.add(savePayDetail);
            saveUserPay.setPayDetailList(payDetailList);
            saveUserPay.setPay(savePayDetail.getTotalPrice());
            userPayRepository.save(saveUserPay);
        } else {
            List<PayDetail> payDetailList = userPay.getPayDetailList();
            int count = 0;
            for (PayDetail item : payDetailList) {
                if (item.getOptionId() == option.getId()) {
                    item.setCount(item.getCount() + 1);
                    item.setTotalPrice(orderPrice * item.getCount());
                    count++;
                }
            }
            if (count == 0) {
                PayDetail savePayDetail = new PayDetail.Builder()
                        .groupId(group.getId())
                        .groupName(group.getName())
                        .storeId(store.getId())
                        .storeName(store.getName())
                        .productId(product.getId())
                        .productName(product.getName())
                        .productPrice(product.getPrice())
                        .optionId(option.getId())
                        .optionName(option.getName())
                        .optionPrice(option.getPrice())
                        .count(1)
                        .totalPrice(orderPrice)
                        .orderId(order.getId())
                        .userPay(userPay)
                        .build();
                payDetailList.add(savePayDetail);
            }
            userPay.setDate(LocalDateTime.now());
            userPay.setPay(userPay.getPay() + orderPrice);
            userPay.setPayDetailList(payDetailList);
            userPayRepository.save(userPay);
        }
    }

    @Override
    @Transactional
    public void delOrderItem(int productId, int groupId) {
        logger.info("OrdService_delOrderItem orderId:{}, groupId:{}", productId, groupId);

        int userIdServer = ContextHolderHandler.getId();

        Optional<User> user = userRepository.findById(userIdServer);
        Group group = groupRepository.getById(groupId);

        canOrder(group, user.get());
        deleteOrder(productId, groupId, userIdServer);

        deleteOnGoingPay(productId, groupId, userIdServer);
    }

    private void deleteOrder(int productId, int groupId, int userId) {
        List<Order> orderList = orderHistoryRepository.getDelOrderHistory(productId, groupId, userId);
        if (orderList == null || orderList.size() == 0) {
            throw new IllegalArgumentException("無點餐資料可刪除");
        }
        orderHistoryRepository.delOrderHistory(productId, groupId, userId);
    }

    private void deleteOnGoingPay(int productId, int groupId, int userId) {
        UserPay userPay = userPayRepository.getByGroupOnGoing(userId, groupId, NOT_CHARGE);
        List<PayDetail> payDetailList = userPay.getPayDetailList();
        List<PayDetail> delItemList = payDetailList.stream()
                .filter(r -> r.getProductId() == productId)
                .collect(Collectors.toList());
        if (payDetailList.size() == delItemList.size()) {
            userPayRepository.delete(userPay);
        } else {
            int sum = delItemList.stream().mapToInt(PayDetail::getTotalPrice).sum();
            userPay.setPay(userPay.getPay() - sum);
            userPay.setDate(LocalDateTime.now());
            userPayRepository.save(userPay);
            payDetailRepository.delByPayIdAndProductId(userPay.getId(), productId);
        }
    }

    @Override
    @Transactional
    public void managerAddOrder(int optionId, int groupId, int userId) {
        logger.info("OrdService_managerAddOrder orderId:{}, groupId:{}, userId:{}", optionId, groupId, userId);

        int userIdServer = ContextHolderHandler.getId();
        User manager = userRepository.getUserById(userIdServer);
        Group group = groupRepository.getById(groupId);
        isGroupManager(group, userIdServer, "加點");

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent())
            throw new IllegalArgumentException("新增失敗，會員資料庫尚未建檔");
        User user = userOptional.get();

        Optional<Option> optionOptional = optionRepository.findById(optionId);
        if (!optionOptional.isPresent())
            throw new IllegalArgumentException("新增失敗，餐點說明資料庫尚未建檔");
        Option option = optionOptional.get();
        Product product = option.getProduct();
        Store store = storeRepository.getStoreByOptionId(optionId);
        int orderPrice = product.getPrice() + option.getPrice();

        if (!haveDeposit(userId, orderPrice))
            throw new IllegalArgumentException("儲值金不足，請加值");

        Order order = addOrder(option, group, store, user);

        managerPay(option, group, store, product, order, user, manager);
    }

    private void managerPay(Option option, Group group, Store store, Product product, Order order, User user, User manager) {
        Timestamp nowTime = new Timestamp(new Date().getTime());
        int orderPrice = product.getPrice() + option.getPrice();
        if (group.isLocked() || group.getEndTime().before(nowTime)) {
            UserPay saveUserPay = new UserPay.Builder()
                    .status(DEDUCTION)
                    .payType(0)
                    .date(LocalDateTime.now())
                    .manager(manager.getEnglishName())
                    .debit(CHARGE)
                    .user(user)
                    .build();
            PayDetail savePayDetail = new PayDetail.Builder()
                    .groupId(group.getId())
                    .groupName(group.getName())
                    .storeId(store.getId())
                    .storeName(store.getName())
                    .productId(product.getId())
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .optionId(option.getId())
                    .optionName(option.getName())
                    .optionPrice(option.getPrice())
                    .count(1)
                    .totalPrice(orderPrice)
                    .orderId(order.getId())
                    .userPay(saveUserPay)
                    .build();
            List<PayDetail> payDetailList = new ArrayList<>();
            payDetailList.add(savePayDetail);
            saveUserPay.setPayDetailList(payDetailList);
            saveUserPay.setPay(savePayDetail.getTotalPrice());
            userPayRepository.save(saveUserPay);
        } else {
            noChargePay(option, group, store, product, order, user);
        }
    }

    @Override
    @Transactional
    public void managerDeleteOrder(int productId, int groupId, int userId) {
        logger.info("OrdService_managerDeleteOrder productId:{}, groupId:{}, userId:{}", productId, groupId, userId);
        int userIdServer = ContextHolderHandler.getId();
        User manager = userRepository.getUserById(userIdServer);
        Group group = groupRepository.getById(groupId);
        isGroupManager(group, userIdServer, "刪除");

        deleteOrder(productId, groupId, userId);
        User user = userRepository.getUserById(userId);

        Timestamp nowTime = new Timestamp(new Date().getTime());
        if (group.isLocked() || group.getEndTime().before(nowTime)) {
            List<UserPay> userPayList = userPayRepository.getByGroupAndUser(userId, groupId, CHARGE);
            StringBuilder userPayInfo = new StringBuilder();
            for (UserPay item: userPayList) {
                userPayInfo.append(item.toString());
            }
            logger.info("userPay = " + userPayInfo.toString());
            List<PayDetail> payDetailList = userPayList.stream()
                    .map(UserPay::getPayDetailList)
                    .flatMap(List::stream).collect(Collectors.toList());
            List<PayDetail> deleteItemList = payDetailList.stream()
                    .filter(r -> r.getProductId() == productId && !r.isManagerDeleted())
                    .collect(Collectors.toList());

            StringBuilder delPayDetailInfo = new StringBuilder();
            for (PayDetail item : deleteItemList) {
                delPayDetailInfo.append(item.toString());
            }
            logger.info("payDetail = " + delPayDetailInfo.toString());

            int backPay = 0;
            for (PayDetail item : deleteItemList) {
                item.setManagerDeleted(true);
                backPay += item.getTotalPrice();
            }
            refundOfStoredValue(group, manager, user, backPay);
        } else {
            deleteOnGoingPay(productId, groupId, userId);
        }
    }

    void refundOfStoredValue(Group group, User manager, User user, int backPay) {
        Date date = new Date();
        date.setTime(group.getOpenDate().getTime());
        String formatOpenDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String deleteRemark = formatOpenDate + " " + group.getName() + " 已刪單，退回儲值金";
        UserPay saveUserPay = new UserPay.Builder()
                .status(ADD_VALUE)
                .payType(0)
                .date(LocalDateTime.now())
                .manager(manager.getEnglishName())
                .debit(CHARGE)
                .user(user)
                .pay(backPay)
                .remark(deleteRemark)
                .build();
        userPayRepository.save(saveUserPay);
    }

    boolean canOrder(Group group, User user) {
        if (!group.isReleaseTime())
            throw new RuntimeException("此團尚未開始，或是已經結束");
        if (group.isLocked() && !group.getUserList().contains(user))
            throw new RuntimeException("已結單，只有管理員可以點餐");
        haveGroupTeam(group, user);
        return true;
    }

    boolean haveDeposit(int userId, int orderPrice) {
        Integer userDeposit = userPayRepository.getBalance(userId) == null ? 0 : userPayRepository.getBalance(userId);
        return userDeposit >= orderPrice;
    }

    @Override
    @Transactional
    public void addOptionAndOrder(OptionAddRequestV2_1 request) {
        logger.info("OrdService_addOneOption call");

        int userIdServer = ContextHolderHandler.getId();
        Optional<User> userOptional = userRepository.getById(userIdServer);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("新增失敗，會員資料庫尚未建檔");
        }
        User user = userOptional.get();

        Optional<Product> productOptional = orderRepository.findById(request.getProductId());
        if (!productOptional.isPresent()) {
            throw new IllegalArgumentException("新增失敗，餐點資料庫尚未建檔");
        }
        Product product = productOptional.get();

        if (!haveDeposit(userIdServer, product.getPrice() + request.getPrice()))
            throw new IllegalArgumentException("儲值金不足，請加值");

        Store store = storeRepository.getByProductId(request.getProductId());
        Option option = addOption(request.getName(), request.getPrice(), product);
        Group group = groupRepository.getById(request.getGroupId());

        haveGroupTeam(group, user);
        Order order = addOrderByOption(group, store, option, user);

        noChargePay(option, group, store, product, order, user);
    }

    @Override
    @Transactional
    public void managerAddOptionAndOrder(OptionAddRequestV2_1 request) {

        logger.info("OrdService_managerAddOneOption call");

        int userIdServer = ContextHolderHandler.getId();
        User manager = userRepository.getUserById(userIdServer);
        Optional<User> userOptional = userRepository.getById(request.getUserId());
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("新增失敗，會員資料庫尚未建檔");
        }
        User user = userOptional.get();

        Optional<Product> productOptional = orderRepository.findById(request.getProductId());
        if (!productOptional.isPresent()) {
            throw new IllegalArgumentException("新增失敗，餐點資料庫尚未建檔");
        }
        Product product = productOptional.get();

        if (!haveDeposit(userIdServer, product.getPrice() + request.getPrice()))
            throw new IllegalArgumentException("儲值金不足，請加值");

        Store store = storeRepository.getByProductId(request.getProductId());
        Option option = addOption(request.getName(), request.getPrice(), product);
        Group group = groupRepository.getById(request.getGroupId());
        isGroupManager(group, userIdServer, "加點");

        Order order = addOrderByOption(group, store, option, user);

        managerPay(option, group, store, product, order, user, manager);
    }

    private Order addOrderByOption(Group group, Store store, Option option, User user) {

        List<User> userList = new ArrayList<>();
        userList.add(user);
        Order order = new Order();
        order.setGroup(group);
        order.setUserList(userList);
        order.setDate(new Timestamp(new Date().getTime()));
        order.setOption(option);
        order.setStore(store);
        return orderHistoryRepository.save(order);
    }

    private Option addOption(String name, int price, Product product) {
        logger.info("OptionService_addOption call");

        name = name.trim();
        if (optionRepository.existsByNameAndPriceAndProductId(name, price, product.getId())) {
            throw new IllegalArgumentException("新增失敗，說明已存在");
        }

        Option option = new Option(name, price, product);
        return optionRepository.save(option);
    }

    @Override
    @Transactional
    public void deleteOption(int optionId, int groupId) {
        Optional<Option> option = optionRepository.findById(optionId);
        if (!option.isPresent()) {
            throw new IllegalArgumentException("此餐點說明不存在");
        }

        Group group = groupRepository.getById(groupId);
        Timestamp nowTime = new Timestamp(new Date().getTime());
        if (!group.isLocked() && group.getEndTime().after(nowTime)) {
            List<UserPay> updateUserPayList = userPayRepository.getByOptionAndGroup(optionId, groupId);
            List<UserPay> delUserPayList = new ArrayList<>();
            for (UserPay item : updateUserPayList) {
                List<PayDetail> payDetailList = item.getPayDetailList();
                if (payDetailList.size() == 1) {
                    delUserPayList.add(item);
                } else {
                    PayDetail payDetail = payDetailList.stream().filter(r -> r.getOptionId() == optionId).findFirst().get();
                    item.setPay(item.getPay() - payDetail.getTotalPrice());
                    payDetailRepository.deleteById(payDetail.getId());
                }
            }
            userPayRepository.deleteAll(delUserPayList);
        }
        optionRepository.deleteById(optionId);
    }

    private void haveGroupTeam(Group group, User user) {
        List<Team> userTeamList = user.getTeamList();
        List<Team> groupTeamList = group.getTeamList();
        if (userTeamList.stream().noneMatch(groupTeamList::contains))
            throw new IllegalArgumentException("不屬於這個團設定的群組，所以無法點餐，有需要請洽管理員");
    }

    void isGroupManager(Group group, int userId, String message) {
        List<Integer> groupUser = group.getUserList().stream().map(User::getId).collect(Collectors.toList());
        if (!groupUser.contains(userId)) {
            throw new IllegalArgumentException("本用戶沒有權限可以" + message);
        }
    }

    @Override
    @Transactional
    public void initPayRecord() {
        List<Group> byOnOpen = groupRepository.getByOnOpen();

        for (Group group : byOnOpen) {
            List<Order> orderList = group.getOrderList();
            for (Order order : orderList) {
                Option option = order.getOption();
                Product product = option.getProduct();
                Store store = product.getStore();
                List<User> userList = order.getUserList();
                User manager = new User();
                manager.setEnglishName("N/A");
                for (User user : userList) {
                    managerPay(option, group, store, product, order, user, manager);
                }
            }
        }
    }
}
