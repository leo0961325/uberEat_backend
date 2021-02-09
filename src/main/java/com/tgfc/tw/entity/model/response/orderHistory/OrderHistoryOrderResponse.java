package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.po.Product;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

public class OrderHistoryOrderResponse {

    private int id;
    private String name;
    private String remark;
    private int price;
    private int count;
    private int optionAllPrice;
    private int orderTotalPrice;
    private List<OrderHistoryUserRespense> users;
    private boolean canDelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<OrderHistoryUserRespense> getUsers() {
        return users;
    }

    public void setUsers(List<OrderHistoryUserRespense> users) {
        this.users = users;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(int orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public int getOptionAllPrice() {
        return optionAllPrice;
    }

    public void setOptionAllPrice(int optionAllPrice) {
        this.optionAllPrice = optionAllPrice;
    }

    public static List<OrderHistoryOrderResponse> valueListOf(List<Product> productList, List<OrderHistoryUserRespense> histories) {
        List<OrderHistoryOrderResponse> list = new ArrayList<>();

        for (Product product : productList) {
            List<OrderHistoryUserRespense> orderHistoryUserResponseList = histories.stream()
                    .filter(r -> product.getOptionList().stream().map(q -> q.getId()).collect(Collectors.toList()).contains(r.getOptionId()))
                    .collect(Collectors.toList());

            OrderHistoryOrderResponse newOrders = OrderHistoryOrderResponse.valueOf(product, orderHistoryUserResponseList);
            list.add(newOrders);
        }

        return list.stream()
                .sorted(Comparator.comparing(OrderHistoryOrderResponse::getName)
                        .thenComparing(OrderHistoryOrderResponse::getId))
                .collect(Collectors.toList());
    }

    private static OrderHistoryOrderResponse valueOf(Product product, List<OrderHistoryUserRespense> histories) {
        OrderHistoryOrderResponse newOrders = new OrderHistoryOrderResponse();
        int orderCount = 0;
        for (OrderHistoryUserRespense item : histories) {
            orderCount += item.getCount();
            newOrders.optionAllPrice += item.getOptionTotalPrice();
        }
        BeanUtils.copyProperties(product, newOrders);
        newOrders.setCount(orderCount);
        newOrders.setOptionAllPrice(newOrders.optionAllPrice);
        newOrders.setOrderTotalPrice(orderCount*newOrders.price+newOrders.optionAllPrice);
        newOrders.setUsers(histories);
        newOrders.setCanDelete(product.canDelete());
        return newOrders;
    }


}
