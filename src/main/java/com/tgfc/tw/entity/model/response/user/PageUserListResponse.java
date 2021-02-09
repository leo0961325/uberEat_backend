package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.model.po.Floor;
import com.tgfc.tw.entity.model.po.Team;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.po.UserPay;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.TeamResponse;

import java.util.ArrayList;
import java.util.List;

public class PageUserListResponse {

    private Integer id;
    private String userName;
    private String name;
    private String englishName;
    private List<TeamResponse> teamList;
    private Integer deposit;

    public PageUserListResponse(User user) {
        this.id = user.getId();
        this.userName = user.getUsername();
        this.name = user.getName();
        this.englishName = user.getEnglishName();

        List<TeamResponse> addList = new ArrayList<>();
        user.getTeamList().forEach(r -> {
            if(user.getTeamList() != null && !r.isDeleted()){
                TeamResponse t = new TeamResponse();
                t.setId(r.getId());
                t.setName(r.getName());
                addList.add(t);

            }
        });
        this.teamList = addList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public List<TeamResponse> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<TeamResponse> teamList) {
        this.teamList = teamList;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }
}
