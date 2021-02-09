package com.tgfc.tw.entity.model.po;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "floor")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "group_floor",
            joinColumns = {@JoinColumn(name = "floor_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private List<Group> groupList;


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

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }


    @Override
    public String toString() {
        return "Floor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
    //
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    private List<StoreReview> storeReviewList;

}
