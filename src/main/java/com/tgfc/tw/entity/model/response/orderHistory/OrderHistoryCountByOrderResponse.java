package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.po.Order;
import com.tgfc.tw.entity.model.po.Product;
import com.tgfc.tw.entity.model.response.OptionCountResponse;

import java.util.*;
import java.util.stream.Collectors;

public class OrderHistoryCountByOrderResponse {
    private int id;
    private String name;
    private int price;
    private long count;
    private long totalPrice;
    private List<OptionCountResponse> content;

    public OrderHistoryCountByOrderResponse(int id, String name, int price, long count, long totalPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.totalPrice = totalPrice;
    }

    public OrderHistoryCountByOrderResponse() {
    }

    public OrderHistoryCountByOrderResponse(Product product, Order oh) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        List<OptionCountResponse> optionList = OptionCountResponse.valueOf(product.getOptionList(),oh);
        this.content = optionList;
        optionList.stream().forEach(o->this.count+=o.getCount());
        int optionTotal = this.content.stream().mapToInt(OptionCountResponse::getTotalPrice).sum();
        this.totalPrice = this.price * this.count + optionTotal;
    }

    public static List<OrderHistoryCountByOrderResponse> valueOf(List<OrderHistoryCountByOrderResponse> data) {
        List<OrderHistoryCountByOrderResponse> result = new ArrayList<>();
        OrderHistoryCountByOrderResponse order = new OrderHistoryCountByOrderResponse();

        for (OrderHistoryCountByOrderResponse item : data) {
            long i = data.stream().filter(d -> d.name.equals(item.name)).count();
//            List<OptionCountResponse> optionSet = data.stream().filter(d -> d.name.equals(item.name)).collect(Collectors.toList());
            List<OrderHistoryCountByOrderResponse> same = data.stream().filter(d -> d.name.equals(item.name)).collect(Collectors.toList());
            OrderHistoryCountByOrderResponse response = new OrderHistoryCountByOrderResponse();
            if (i > 1 && (order.name == null || !order.content.equals(same))) {
                response.name = item.name;
                order.name = item.name;
                same.stream().forEach(r -> response.count += r.count);
                same.stream().forEach(r -> response.totalPrice += r.totalPrice);
//                response.content = same;
//                order.content = same;
                result.add(response);
            } else if (i < 2) {
                result.add(item);
            }
        }
        return result;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OptionCountResponse> getContent() {
        return content;
    }

    public void setContent(List<OptionCountResponse> content) {
        this.content = content;
    }
}
