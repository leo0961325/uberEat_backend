package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.*;
import com.tgfc.tw.entity.model.request.OptionAddRequest;
import com.tgfc.tw.entity.model.request.OptionUpdateRequest;
import com.tgfc.tw.entity.model.response.OptionCountResponse;
import com.tgfc.tw.entity.model.response.OptionResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponse;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.OptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OptionServiceImpl implements OptionService {
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OptionRepository optionRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    private static Logger logger = LoggerFactory.getLogger(OptionService.class);

    @Override
    @Transactional
    public Option addOption(String name, int price, int productId) throws Exception {

        logger.info("OptionService_addOption call");

        Optional<Product> order = orderRepository.findById(productId);
        if (!order.isPresent()) {
            throw new Exception("新增失敗，餐點資料庫尚未建檔");
        }
        if (optionRepository.existsByNameAndPriceAndProductId(name, price, productId)) {
            throw new Exception("新增失敗，說明已存在");
        }

        Option option = new Option(name, price, order.get());

        return optionRepository.save(option);
    }

    @Override
    @Transactional
    public void addOneOption(OptionAddRequest request) throws Exception {

        logger.info("OptionService_addOneOption call");

        int userIdServer = ContextHolderHandler.getId();
        addOptionByUser(request, userIdServer);

    }

    @Override
    @Transactional
    public void managerAddOneOption(OptionAddRequest request) throws Exception {

        logger.info("OptionService_managerAddOneOption call");

        int userIdServer = ContextHolderHandler.getId();
        Group group = groupRepository.getById(request.getGroupId());
        List<Integer> groupUser = group.getUserList().stream().map(r -> r.getId()).collect(Collectors.toList());
        if (!groupUser.contains(userIdServer)) {
            throw new Exception("本用戶沒有權限可以加點");
        }
        addOptionByUser(request, request.getUserId());

    }

    private void addOptionByUser(OptionAddRequest request, int userId) throws Exception {
        request.setName(request.getName().trim());
        Optional<User> user = userRepository.getById(userId);
        if (!user.isPresent()) {
            throw new Exception("新增失敗，會員資料庫尚未建檔");
        }
        List<User> userList = new ArrayList<>();
        userList.add(user.get());

        Group group = groupRepository.getById(request.getGroupId());
        List<Integer> floorId = group.getFloorList().stream().map(r -> r.getId()).collect(Collectors.toList());

        if (!floorId.contains(ContextHolderHandler.getTeamIdList())) {
            throw new Exception("你不在團所屬樓層!無法新增餐點!");
        }


        Order order = new Order();
        order.setGroup(group);
        order.setUserList(userList);
        order.setDate(new Timestamp(new Date().getTime()));
        Option option = addOption(request.getName(), request.getPrice(), request.getProductId());
        order.setOption(option);
        order.setStore(storeRepository.getStoreByOptionId(option.getId()));
        orderHistoryRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOption(int id) throws Exception {

        Optional<Option> option = optionRepository.findById(id);
        if (!option.isPresent()) {
            throw new Exception("此id不存在");
        }
        optionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateOption(OptionUpdateRequest request) throws Exception {
        request.setName(request.getName().trim());
        Optional<Option> option = optionRepository.findById(request.getId());
        if (!option.isPresent()) {
            throw new Exception("此id不存在");
        } else if (option.get().getName().equals(request.getName()) && option.get().getPrice() == request.getPrice()) {
            return;
        }
        Option newOption = option.get();
        if (optionRepository.existsByNameAndPriceAndProductId(request.getName(), request.getPrice(), newOption.getProduct().getId())) {
            throw new Exception("修改失敗，說明已存在");
        } else {
            newOption.setName(request.getName());
            newOption.setPrice(request.getPrice());
            optionRepository.save(newOption);
        }
    }

    @Override
    public List<OptionResponse> getOptionByOrder(int productId) {
        return optionRepository.getByProductId(productId);
    }

    @Override
    public OptionResponse getOptionById(int optionId) throws Exception {

        Optional<Option> option = optionRepository.findById(optionId);
        if (!option.isPresent()) {
            throw new Exception("此餐點說明不存在");
        }

        return optionRepository.getById(optionId);
    }

    @Override
    public List<OptionCountResponse> getOptionByGroup(int groupId, int productId) throws Exception {

        Optional<User> user = userRepository.findById(ContextHolderHandler.getId());
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<Product> order = orderRepository.findById(productId);

        if (!group.isPresent()) throw new Exception("此團不存在");
        if (!order.isPresent()) throw new Exception("此餐點項目不存在");
        if (!user.isPresent()) throw new Exception("無此使用者");

        User currentUser = user.get();
        UserNameResponse ur = new UserNameResponse(currentUser);

        List<Order> userOrderList = currentUser.getOrderList();
        List<Map<String,Object>> getOptions = optionRepository.getByGroup(groupId, productId);
        List<OptionCountResponse> result = new ArrayList<>();
        for (Map<String,Object> mOption : getOptions) {

            OptionCountResponse ocr = new OptionCountResponse(
                    (int)mOption.get("id"),
                    (String)mOption.get("name"),
                    (int)mOption.get("price"),
                    (int)mOption.get("productPrice"),
                    ((BigInteger)mOption.get("orderCount")).intValue());

            for (Order oh : userOrderList) {
                if (oh.getOption().getId() == ocr.getId() && oh.getGroup().getId() == groupId) {
                    ocr.setUser(ur);
                }
            }
            result.add(ocr);
        }

        return result;
    }
}
