package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.TeamResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDevResponseV2_1 {
    private Integer id;
    private String username;
    private String name;
    private String englishName;
    private List<Integer> teamIdList;
    private List<TeamResponse> allTeamList;
    private Map<Integer, String> permissions;

    public UserDevResponseV2_1() {
    }

    public static UserDevResponseV2_1 valueOfPermission(User user) {

        List<Integer> teamIdList = new ArrayList<>();
        UserDevResponseV2_1 response = new UserDevResponseV2_1();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEnglishName(user.getEnglishName());

        user.getTeamList().forEach(t->teamIdList.add(t.getId()));
        response.setTeamIdList(teamIdList);

        Map<Integer, String> map = new HashMap<>();
        user.getRoleList().forEach(r -> map.put(r.getId(), r.getName()));
        if (map.size() == 0) {
            map.put(PermissionEnum.ROLE_NORMAL.getPermissionOrder(), PermissionEnum.RoleUserName.NORMAL_USER_NAME);
            response.setPermissions(map);
        } else {
            response.setPermissions(map);
        }
        return response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<TeamResponse> getAllTeamList() {
        return allTeamList;
    }

    public void setAllTeamList(List<TeamResponse> allTeamList) {
        this.allTeamList = allTeamList;
    }

    public Map<Integer, String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<Integer, String> permissions) {
        this.permissions = permissions;
    }
}
