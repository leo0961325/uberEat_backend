package com.tgfc.tw.entity.model.response.orderHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgfc.tw.entity.model.po.Order;
import com.tgfc.tw.entity.model.po.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderHistoryUserRespense {
    private int id;
    private String name;
    @JsonProperty("floor")
    private String floorId;
    private int optionId;
    private int count;
    private int optionPrice;
    private int optionTotalPrice;

    public static List<OrderHistoryUserRespense> valueListOf(List<Order> orderHistories, int userFloorId, boolean isManager) {
        List<OrderHistoryUserRespense> list = new ArrayList<>();
        for(Order history : orderHistories){
            //orderHistories內的info是從各表格現有資料中經層層篩選取得最後要的部分 因此透過關聯性資料本來就存在
            for(User user : history.getUserList()) {  //先取同樓層 但樓層是在user裡 所以從user先取
                //不同樓層不取出來
                if(!isManager && userFloorId != user.getFloor().getId())
                    continue;
                Optional<OrderHistoryUserRespense> temp = list.stream() //同樓層進入比對使用者id和點餐項目
                        .filter(r -> r.getId() == user.getId() && r.getOptionId() == history.getOption().getId())  //A點1號餐
                        .findFirst();  //與OPTIONAL對應 只取一筆資料
                if(temp.isPresent()){
                    temp.get().setCount(temp.get().getCount() + 1);  //若完全符合26行內容+1  此行計算數量
                    continue;
                }
                OrderHistoryUserRespense newItem = new OrderHistoryUserRespense();
                newItem.setId(user.getId());
                newItem.setOptionId(history.getOption().getId());
                newItem.setOptionPrice(history.getOption().getPrice());
                newItem.setName(user.getEnglishName());
                newItem.setFloorId(user.getFloor().getName());
                newItem.setCount(1);
                newItem.setOptionTotalPrice(newItem.optionPrice*newItem.count);
                list.add(newItem);
            }
        }
        return list;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(int optionPrice) {
        this.optionPrice = optionPrice;
    }

    public int getOptionTotalPrice() {
        return optionTotalPrice;
    }

    public void setOptionTotalPrice(int optionTotalPrice) {
        this.optionTotalPrice = optionTotalPrice;
    }
}
