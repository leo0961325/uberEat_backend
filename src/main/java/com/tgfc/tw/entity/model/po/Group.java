package com.tgfc.tw.entity.model.po;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //對應DB裡面，預設選項"□自動遞增"要打勾
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "open_date")
    private Timestamp openDate;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name="group_user",joinColumns= {@JoinColumn(name="group_id")},inverseJoinColumns= {@JoinColumn(name="user_id")})
    private List<User> userList;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name="group_store",joinColumns= {@JoinColumn(name="group_id")},inverseJoinColumns= {@JoinColumn(name="store_id")})
    @OrderBy("id ASC")
    private List<Store> storeList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<Order> orderList;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "group_floor",joinColumns= {@JoinColumn(name="group_id")},inverseJoinColumns= {@JoinColumn(name="floor_id")})
    private List<Floor> floorList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group",fetch = FetchType.EAGER)
    private List<OrderPass> orderPassList;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "group_team",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id")})
    private List<Team> teamList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public boolean isLocked() {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        if(this.endTime != null && this.endTime.before(currentTime))
            return true;
        else
            return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public boolean isReleaseTime(){
        Timestamp currentTime = new Timestamp(new Date().getTime());
        return this.startTime.before(currentTime) && currentTime.before(this.endTime);
    }

    public List<Floor> getFloorList() {
        return floorList;
    }

    public void setFloorList(List<Floor> floorList) {
        this.floorList = floorList;
    }

    public List<OrderPass> getOrderPassList() {
        return orderPassList;
    }

    public void setOrderPassList(List<OrderPass> orderPassList) {
        this.orderPassList = orderPassList;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Timestamp getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Timestamp openDate) {
        this.openDate = openDate;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }
}
