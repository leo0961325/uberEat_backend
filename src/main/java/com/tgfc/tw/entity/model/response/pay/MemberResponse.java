package com.tgfc.tw.entity.model.response.pay;

import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.TeamResponse;

import java.util.List;

public class MemberResponse {

    private String account;
    private String name;
    private String englishName;
    private int balance;
    private List<TeamResponse> team;

    public static MemberResponse valueOf(User user, int balance, List<TeamResponse> teamList) {
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setAccount(user.getUsername());
        memberResponse.setName(user.getName());
        memberResponse.setEnglishName(user.getEnglishName());
        memberResponse.setBalance(balance);
        memberResponse.setTeam(teamList);
        return memberResponse;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<TeamResponse> getTeam() {
        return team;
    }

    public void setTeam(List<TeamResponse> team) {
        this.team = team;
    }
}
