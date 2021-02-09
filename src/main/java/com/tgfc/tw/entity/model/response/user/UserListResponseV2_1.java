package com.tgfc.tw.entity.model.response.user;

import org.springframework.data.domain.Page;

public class UserListResponseV2_1 {

    private Integer totalDeposit;
    private Page<PageUserListResponse> userListResponsePage;

    public Integer getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(Integer totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public Page<PageUserListResponse> getUserListResponsePage() {
        return userListResponsePage;
    }

    public void setUserListResponsePage(Page<PageUserListResponse> userListResponsePage) {
        this.userListResponsePage = userListResponsePage;
    }
}
