package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.po.Order;
import com.tgfc.tw.entity.model.po.Product;
import com.tgfc.tw.entity.model.response.OptionCountResponseV2_1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderHistoryCountByOrderResponseV2_1 {
    private int id;
    private String name;
    private int price;
    private long count;
    private long totalPrice;
    private List<OptionCountResponseV2_1> content;

    public OrderHistoryCountByOrderResponseV2_1(int id, String name, int price, long count, long totalPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.totalPrice = totalPrice;
    }

    public OrderHistoryCountByOrderResponseV2_1() {
    }

    public OrderHistoryCountByOrderResponseV2_1(Product product, Order oh) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        List<OptionCountResponseV2_1> optionList = OptionCountResponseV2_1.valueOf(product.getOptionList(),oh);
        this.content = optionList;
        optionList.stream().forEach(o->this.count+=o.getCount());
        int optionTotal = this.content.stream().mapToInt(OptionCountResponseV2_1::getTotalPrice).sum();
        this.totalPrice = this.price * this.count + optionTotal;
    }

    public static List<OrderHistoryCountByOrderResponseV2_1> valueOf(List<OrderHistoryCountByOrderResponseV2_1> data) {
        List<OrderHistoryCountByOrderResponseV2_1> result = new ArrayList<>();
        OrderHistoryCountByOrderResponseV2_1 order = new OrderHistoryCountByOrderResponseV2_1();

        for (OrderHistoryCountByOrderResponseV2_1 item : data) {
            long i = data.stream().filter(d -> d.name.equals(item.name)).count();
            List<OrderHistoryCountByOrderResponseV2_1> same = data.stream().filter(d -> d.name.equals(item.name)).collect(Collectors.toList());
            OrderHistoryCountByOrderResponseV2_1 response = new OrderHistoryCountByOrderResponseV2_1();
            if (i > 1 && (order.name == null || !order.content.equals(same))) {
                response.name = item.name;
                order.name = item.name;
                same.stream().forEach(r -> response.count += r.count);
                same.stream().forEach(r -> response.totalPrice += r.totalPrice);
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

    public List<OptionCountResponseV2_1> getContent() {
        return content;
    }

    public void setContent(List<OptionCountResponseV2_1> content) {
        this.content = content;
    }
}
