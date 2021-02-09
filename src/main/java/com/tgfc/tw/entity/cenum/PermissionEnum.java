package com.tgfc.tw.entity.cenum;

public enum PermissionEnum {
    ROLE_SUPER_MANAGER(1,Role.SUPER_MANAGER),
    ROLE_MANAGER(2,Role.MANAGER),
    ROLE_NORMAL(-1,Role.NORMAL),
    ROLE_NORMAL_RECOMMEND(4,Role.NORMAL_RECOMMEND);

    public class Role{
        public static final String SUPER_MANAGER= "ROLE_SUPER_MANAGER";
        public static final String MANAGER= "ROLE_MANAGER";
        public static final String NORMAL= "ROLE_NORMAL";
        public static final String NORMAL_RECOMMEND= "ROLE_NORMAL_RECOMMEND";
    }

    public class RoleUserName{
        //public static final String SUPER_MANAGER_USER_NAME= "super_admin";
        //public static final String SUPER_MANAGER_USER_NAME= "admin";
        public static final String NORMAL_USER_NAME= "一般使用者";
    }

    int permissionOrder;
    String permissionName;

    PermissionEnum(int permissionOrder,String permissionName){
        this.permissionOrder=permissionOrder;
        this.permissionName=permissionName;
    }

    public int getPermissionOrder() {
        return permissionOrder;
    }

    public void setPermissionOrder(int permissionOrder) {
        this.permissionOrder = permissionOrder;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
