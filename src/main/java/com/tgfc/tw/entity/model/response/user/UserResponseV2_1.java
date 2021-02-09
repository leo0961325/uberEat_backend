package com.tgfc.tw.entity.model.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.po.Role;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.TeamResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserResponseV2_1 {
    private int id;
    private String username;
    private String name;
    private String englishName;
    private List<Integer> teamIdList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TeamResponse> allTeams;
    private int floorId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FloorResponse> allFloors;
    private List<String> permissions;

    public UserResponseV2_1() {}

    public UserResponseV2_1(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

    public static UserResponseV2_1 valueOf(User user) {
        UserResponseV2_1 response = new UserResponseV2_1();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEnglishName(user.getEnglishName());
        response.setFloorId(user.getFloor().getId());

        List<Integer> addTeamIdList = new ArrayList<>();
        user.getTeamList().forEach(r -> {
            if (user.getTeamList() != null) {
                addTeamIdList.add(r.getId());
            }
        });
        response.setTeamIdList(addTeamIdList);

        return response;
    }

    public static UserResponseV2_1 valueOfPermission(User user) {
        UserResponseV2_1 response = new UserResponseV2_1();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEnglishName(user.getEnglishName());
        response.setFloorId(user.getFloor() != null ? user.getFloor().getId() : -1);
        List<Integer> addTeamIdList = new ArrayList<>();
        user.getTeamList().forEach(r -> {
            if (user.getTeamList() != null && !r.isDeleted()) {
                    addTeamIdList.add(r.getId());
            }
        });
        response.setTeamIdList(addTeamIdList);
        response.setPermissions(getPermissionsList(user));

        return response;
    }

    private static UserResponseV2_1 getData(User user){

        UserResponseV2_1 response = new UserResponseV2_1();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEnglishName(user.getEnglishName());

        List<Integer> addTeamIdList = new ArrayList<>();
        user.getTeamList().forEach(r -> {
            if (user.getTeamList() != null) {
                addTeamIdList.add(r.getId());
            }
        });
        response.setTeamIdList(addTeamIdList);
        response.setPermissions(getPermissionsList(user));

        return response;
    }

    public static List<String> getPermissionsList(User user) {
        List<String> permissions = new ArrayList<>();
        List<Role> roles = user.getRoleList();

        if (roles != null && roles.size() > 0) {
            permissions.addAll(roles.stream().map(Role::getCode).collect(Collectors.toList()));
        } else {
            permissions.add(PermissionEnum.Role.NORMAL);
        }

        return permissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<TeamResponse> getAllTeams() {
        return allTeams;
    }

    public void setAllTeams(List<TeamResponse> allTeams) {
        this.allTeams = allTeams;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public List<FloorResponse> getAllFloors() {
        return allFloors;
    }

    public void setAllFloors(List<FloorResponse> allFloors) {
        this.allFloors = allFloors;
    }
}
