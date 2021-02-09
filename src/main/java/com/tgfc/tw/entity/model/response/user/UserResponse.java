package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.po.Role;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.FloorResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserResponse {
    private int id;
    private String username;
    private String name;
    private String englishName;
    private int floorId;
    private List<FloorResponse> allFloors;
    private List<String> permissions;

    public UserResponse() {}

    public UserResponse(User user) {
        this.id=user.getId();
        this.name=user.getName();
    }

    public static UserResponse valueOf(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEnglishName(user.getEnglishName());
        response.setFloorId(user.getFloor().getId());
        return response;
    }

    public static UserResponse valueOfPermission(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEnglishName(user.getEnglishName());
        response.setFloorId(user.getFloor() != null ? user.getFloor().getId() : -1);
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

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
