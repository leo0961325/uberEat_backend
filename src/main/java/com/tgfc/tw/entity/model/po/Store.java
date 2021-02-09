package com.tgfc.tw.entity.model.po;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tgfc.tw.entity.model.request.store.StoreListRequest;
import com.tgfc.tw.entity.model.request.store.StoreRequest;
import com.tgfc.tw.entity.model.request.store.StoreRequestV2_1;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "tel")
    private String tel;

    @Column(name = "address")
    private String address;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "store_tag", joinColumns = {@JoinColumn(name = "store_id")}, inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tagList;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "group_store", joinColumns = {@JoinColumn(name = "store_id")}, inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private List<Group> groupList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<StorePicture> storePictureList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<Product> productList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "store")
    @JsonBackReference
    private List<StoreReview> storeReviewList;

    @Column(name = "remark")
    private String remark;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotEmpty(message = "名稱不得為空")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "電話不得為空")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<StorePicture> getStorePictureList() {
        return storePictureList;
    }

    public void setStorePictureList(List<StorePicture> storePictureList) {
        this.storePictureList = storePictureList;
    }

    public List<StoreReview> getStoreReviewList() {
        return storeReviewList;
    }

    public void setStoreReviewList(List<StoreReview> storeReviewList) {
        this.storeReviewList = storeReviewList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public static StoreListRequest toStoreListRequest(Store store) {

        StoreListRequest storeListRequest = new StoreListRequest();
        storeListRequest.setRemark(store.getRemark());
        storeListRequest.setStoreTel(store.getTel());
        storeListRequest.setStoreName(store.getName());
        storeListRequest.setStoreAddress(store.getAddress());

        return storeListRequest;
    }

    public Store() {

    }

    public Store(StoreRequest storeRequest) {
        this.name = storeRequest.getStoreName();
        this.tel = storeRequest.getStoreTel();
        this.address = storeRequest.getStoreAddress();
        this.remark = storeRequest.getRemark();
    }

    public static Store valueOf(StoreRequestV2_1 request) {
        Store store = new Store();
        store.setName(request.getStoreName());
        store.setTel(request.getStoreTel());
        store.setAddress(request.getStoreAddress());
        store.setRemark(request.getRemark());
        return store;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", storePictureList=" + storePictureList +
                ", storeReviewList=" + storeReviewList +
                ", remark='" + remark + '\'' +
                '}';
    }
}
