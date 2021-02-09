package com.tgfc.tw.entity.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UserPayTotalRequest {

    @NotNull
    @NotEmpty(message = "groupId不能空白")
    private int groupId;

    @NotNull
    @NotEmpty(message = "userId不能空白")
    private int userId;

    @NotNull
    @NotEmpty(message = "storeId不能為空")
    private List<Integer> storeId;


    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getStoreId() {
        return storeId;
    }

    public void setStoreId(List<Integer> storeId) {
        this.storeId = storeId;
    }
}
