package com.tgfc.tw.entity.model.response.user;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class UserUpdateRequestV2_1 {

    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String englishName;

    private List<Integer> teamIdList;

    private List<Integer> permissions;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Integer> getTeamIdList() {
        return teamIdList;
    }

    public void setTeamIdList(List<Integer> teamIdList) {
        this.teamIdList = teamIdList;
    }

    public List<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Integer> permissions) {
        this.permissions = permissions;
    }
}
