package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.FloorResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDevResponse {
    private int id;
    private String username;
    private String name;
    private String englishName;
    private int floorId;
    private List<FloorResponse> allFloors;
    private Map<Integer,String> permissions;

    public UserDevResponse() {

    }

    public UserDevResponse(User user) {
        this.id=user.getId();
        this.name=user.getName();
    }



    public static UserDevResponse valueOfPermission(User user) {
        UserDevResponse response = new UserDevResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEnglishName(user.getEnglishName());
        response.setFloorId(user.getFloor() != null ? user.getFloor().getId() : -1);
        Map<Integer,String> map=new HashMap<>();
        user.getRoleList().stream().forEach(r-> map.put(r.getId(),r.getName()));
        if (map.size() == 0) {
            map.put(PermissionEnum.ROLE_NORMAL.getPermissionOrder(), PermissionEnum.RoleUserName.NORMAL_USER_NAME);
            response.setPermissions(map) ;
        } else {
            response.setPermissions(map);
        }
        return response;
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

    public Map<Integer, String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<Integer, String> permissions) {
        this.permissions = permissions;
    }
}
