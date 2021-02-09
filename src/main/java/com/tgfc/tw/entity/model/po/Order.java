package com.tgfc.tw.entity.model.po;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "order_")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    private Timestamp date;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "option_id", referencedColumnName = "id")
    private Option option;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    private Store store;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name="order_user",joinColumns={@JoinColumn(name="order_id")},inverseJoinColumns= {@JoinColumn(name="user_id")})
    private List<User> userList;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
