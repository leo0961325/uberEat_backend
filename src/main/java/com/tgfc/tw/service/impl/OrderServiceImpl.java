package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.*;
import com.tgfc.tw.entity.model.request.OrderWithoutIdRequest;
import com.tgfc.tw.entity.model.request.OrderWithoutStoreIdRequest;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.order.OrderResponse;
import com.tgfc.tw.entity.model.response.StoreResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponseV2_1;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.OrderHistoryService;
import com.tgfc.tw.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderHistoryRepository orderHistoryRepository;
    @Autowired
    FloorRepository floorRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    OrderPassRepository orderPassRepository;

    @Autowired
    OrderHistoryService orderHistoryService;

    @Autowired
    OptionRepository optionRepository;

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderResponse getOrderById(int orderId) throws Exception {
        Optional<Product> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new Exception("此餐點項目不存在");
        }
        return orderRepository.getById(orderId);
    }

    @Override
    public Page<OrderResponse> orderList(int storeId, int pageNumber, int pageSize, String keyword) {
        pageNumber = (pageNumber >= 1) ? (pageNumber - 1) : 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<OrderResponse> responseList = new ArrayList<>();
        keyword = keyword == null || keyword.isEmpty() ? "%" : "%" + keyword + "%";
        Page<Map<String, Object>> order = orderRepository.findAllByStoreId(storeId, keyword, pageable);
        for (Map m : order) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(Integer.parseInt(m.get("id").toString()));
            orderResponse.setName(m.get("name").toString());
            orderResponse.setPrice(Integer.parseInt(m.get("price").toString()));
            orderResponse.setRemark(m.get("remark")==null?"":m.get("remark").toString());
            responseList.add(orderResponse);
        }
        Page<OrderResponse> orderResponses = new PageImpl<>(responseList, pageable, order.getTotalElements());
        return orderResponses;
    }

    @Override
    @Transactional
    public void add(OrderWithoutIdRequest model) throws Exception {

        //Log 寫入
        logger.info("Service:OrderServiceImpl/Method:add()-Var1 Member:{},{},{},{}",
                model.getName(), model.getPrice(), model.getStoreId(), model.getRemark());

        Optional<Store> store = storeRepository.findById(model.getStoreId());
        if (!store.isPresent())
            throw new IllegalArgumentException("store does not exist");
        else if (orderRepository.existsByNameAndPriceAndStoreIdAndRemark(model.getName(), model.getPrice(), model.getStoreId(), model.getRemark()))
            throw new Exception("project already exists");
        else {
            Product product = new Product(model.getName(), model.getPrice(), model.getRemark(), store.get());
            orderRepository.save(product);
        }
    }

    @Transactional
    public void addNew(OrderWithoutIdRequest model) throws Exception {

        //Log 寫入
        logger.info("Service:OrderServiceImpl/Method:add()-Var1 Member:{}",
                model.toString());

        Optional<Store> store = storeRepository.findById(model.getStoreId());
        int userIdServer = ContextHolderHandler.getId();
        Group group = groupRepository.getById(model.getGroupId());

        if (orderPassRepository.orderPassExist(model.getGroupId(), model.getStoreId(), userIdServer).isPresent())
            throw new Exception("已經PASS，無法新增餐點");

        if (!store.isPresent())
            throw new IllegalArgumentException("store does not exist");
        else if (orderRepository.existsByNameAndPriceAndStoreIdAndRemark(model.getName(), model.getPrice(), model.getStoreId(), model.getRemark()))
            throw new Exception(model.getName() + " project already exists");
        else {
            //新增餐點
            Product product = new Product(model.getName(), model.getPrice(), model.getRemark(), store.get());
            //新增餐點同時寫入點餐紀錄
            orderRepository.save(product);

            orderHistoryService.addOrderItem(product.getId(), model.getGroupId());
        }
    }

    @Transactional
    public void addList(List<OrderWithoutIdRequest> requests) throws Exception {

        //Log 寫入
        for (int i = 0; i < requests.size(); i++) {
            logger.info("Service:OrderServiceImpl/Method:addList()-Var1 Member:{}\n", requests.get(i).getName());
            logger.info("Service:OrderServiceImpl/Method:addList()-Var1 Member:{}\n", requests.get(i).getPrice());
            logger.info("Service:OrderServiceImpl/Method:addList()-Var1 Member:{}\n", requests.get(i).getStoreId());
            logger.info("-------------------\n");
        }

        Optional<Store> store = storeRepository.findById(requests.get(0).getStoreId());
        if (!store.isPresent()) {
            throw new Exception("店家不存在");
        }

        List<OrderWithoutIdRequest> addItems = new ArrayList<>();
        StringBuilder str = new StringBuilder("這些餐點已存在，新增未成功");
        int strLen = str.length();

        HashSet<String> hashSet = new HashSet<String>();
        requests.stream().forEach(r -> hashSet.add(r.getName().replace(" ", "")));
        if (hashSet.size() < requests.size()) {
            throw new Exception("新增餐點有重複");
        }


        requests.stream().forEach(r -> {
            if (orderRepository.getByStoreId(r.getStoreId()).stream().anyMatch(s -> s.getName().replace(" ", "").equals(r.getName().replace(" ", "")))) {
                str.insert(0, r.getName().concat(", "));
            } else {
                addItems.add(r);
            }
        });

        addItems.forEach(r -> {
            Product product = new Product(r.getName(), r.getPrice(), r.getRemark(), store.get());
            Option option = new Option(r.getName(), 0, product);
            List<Option> optionList = new ArrayList<>();
            optionList.add(option);
            product.setOptionList(optionList);
            orderRepository.save(product);
        });
//
        if (str.length() > strLen) {
            throw new Exception(str.toString());
        }
    }

    @Override
    @Transactional
    public void addNewList(List<OrderWithoutIdRequest> requests) throws Exception {

        Optional<Store> store = storeRepository.findById(requests.get(0).getStoreId());
        int userIdServer = ContextHolderHandler.getId();

        if (orderPassRepository.orderPassExist(requests.get(0).getGroupId(), requests.get(0).getStoreId(), userIdServer).isPresent()) {
            throw new Exception("已經PASS，無法新增餐點");
        }

        if (!store.isPresent())
            throw new IllegalArgumentException("store does not exist");


        for (OrderWithoutIdRequest item : requests) {
            if (orderRepository.existsByNameAndPriceAndStoreIdAndRemark(item.getName(), item.getPrice(), item.getStoreId(), item.getRemark()))
                throw new Exception(item.getName() + " project already exists");
        }

        for (OrderWithoutIdRequest item : requests) {
            Product product = new Product(item.getName(), item.getPrice(), item.getRemark(), store.get());
            orderRepository.save(product);
            orderHistoryService.addOrderItem(product.getId(), item.getGroupId());
        }

        for (int i = 0; i < requests.size(); i++) {
            logger.info("Service:OrderServiceImpl/Method:addNewList()-Var1 Member:{}\n", requests.get(i).getName());
            logger.info("Service:OrderServiceImpl/Method:addNewList()-Var1 Member:{}\n", requests.get(i).getPrice());
            logger.info("Service:OrderServiceImpl/Method:addNewList()-Var1 Member:{}\n", requests.get(i).getStoreId());
            logger.info("-------------------\n");
        }
    }

    @Override
    @Transactional
    public void update(OrderWithoutStoreIdRequest model) throws Exception {

        //Log 寫入
        logger.info("Service:OrderServiceImpl/Method:update()-Var1 Member:{},{},{},{}",
                model.getId(), model.getName(), model.getPrice(), model.getRemark());

        if (orderHistoryRepository.getOrderUserAmount(model.getId()) > 0)
            throw new IllegalArgumentException("This Item Can't Be Modified!");

        managerUpdate(model);
    }

    @Override
    @Transactional
    public void managerUpdate(OrderWithoutStoreIdRequest model) throws Exception {

        model.setName(model.getName().trim());
        //Log 寫入
        logger.info("Service:OrderServiceImpl/Method:managerUpdate()-Var1 Member:{},{},{},{}",
                model.getId(), model.getName(), model.getPrice(), model.getRemark());

        Optional<Product> oldOrder = orderRepository.findById(model.getId());
        if (!oldOrder.isPresent())
            throw new IllegalArgumentException("id does not exist");
        Product product = oldOrder.get();
        if (model.isSame(product))
            ;
        else if (orderRepository.existsByNameAndPriceAndStoreIdAndRemark(model.getName(), model.getPrice(), product.getStore().getId(), model.getRemark()))
            throw new Exception("project already exists");
        else {
            product.setName(model.getName());
            product.setPrice(model.getPrice());
            product.setRemark(model.getRemark());
            Option option = optionRepository.getOptionByProductId(model.getId());
            option.setName(model.getName());
            optionRepository.save(option);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        //Log 寫入
        logger.info("Service:OrderServiceImpl/Method:delete()-Var1 Member:{}", id);
        Optional<Product> order = orderRepository.findById(id);

        if (!order.isPresent())
            throw new IllegalArgumentException("id does not exist");
        if (!order.get().canDelete())
            throw new IllegalArgumentException("orderId is using,can't delete it!");
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void managerDelete(int id) {

        logger.info("Service:OrderServiceImpl/Method:managerDelete()-Var1 Member:{}", id);

        Optional<Product> order = orderRepository.findById(id);
        if (!order.isPresent())
            throw new IllegalArgumentException("id does not exist");
        orderRepository.deleteById(id);
    }

    public List<StoreResponse> getStoreList() {
        return storeRepository.findAllForStoreResponse();
    }

    public List<StoreResponse> getStoreList(int groupId) {
        return groupRepository.getStoreNameList(groupId);
    }

    public List<FloorResponse> getFloorList() {
        List<FloorResponse> floorResponseList = floorRepository.getAllFloor();
        return floorResponseList;
    }

    @Deprecated
    public List<UserNameResponse> getUserList(Integer groupId) {
        List<UserNameResponse> userResponseList = userRepository.getAllUserNameList(groupId);
        return userResponseList;
    }

    @Override
    public List<UserNameResponseV2_1> getUserListV2_1(Integer groupId) {
        List<UserNameResponseV2_1> userResponseList = userRepository.getAllUserNameListV2_1(groupId);
        return userResponseList;
    }
}