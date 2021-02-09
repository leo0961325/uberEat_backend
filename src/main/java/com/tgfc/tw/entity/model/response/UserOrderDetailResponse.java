package com.tgfc.tw.entity.model.response;

import java.util.List;

public class UserOrderDetailResponse {


    private String storeName;
    private List<OrderDetailResponse> orderDetailList;
    private int storeSum;

    public UserOrderDetailResponse(String storeName, List<OrderDetailResponse> orderDetailList, int storeSum) {
        this.storeName = storeName;
        this.orderDetailList = orderDetailList;
        this.storeSum = storeSum;
    }

    public int getStoreSum() {
        return storeSum;
    }

    public void setStoreSum(int storeSum) {
        this.storeSum = storeSum;
    }

    public List<OrderDetailResponse> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetailResponse> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public UserOrderDetailResponse(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }



    //    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//
//    public UserOrderDetailResponse(String optionName, BigInteger itemCount, int productPrice, int optionPrice, String productName) {
//        this.optionName = optionName;
//        this.count = itemCount.intValue();
//        this.price = productPrice + optionPrice;
//        this.totalPrice = productPrice + optionPrice;
////        this.storeName = storeName;
//        this.productName = productName;
//    }
//
//    public String getOptionName() {
//        return optionName;
//    }
//
//    public void setOptionName(String optionName) {
//        this.optionName = optionName;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    public int getTotalPrice() {
//        return totalPrice;
//    }
//
//    public void setTotalPrice(int totalPrice) {
//        this.totalPrice = totalPrice;
//    }

//    public String getStoreName() {
//        return storeName;
//    }
//
//    public void setStoreName(String storeName) {
//        this.storeName = storeName;
//    }
}
