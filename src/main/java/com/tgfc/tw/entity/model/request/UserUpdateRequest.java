package com.tgfc.tw.entity.model.request;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class UserUpdateRequest {
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String englishName;
    
    private int floorId;

    private List<Integer> permissions;

    public List<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Integer> permissions) {
        this.permissions = permissions;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

}
