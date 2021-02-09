package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Group;
import com.tgfc.tw.entity.model.po.Product;
import com.tgfc.tw.entity.model.po.Store;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryModel {
    private  List<Group> groups;
    private List<Store> stores;
    private List<Product> products;


    public OrderHistoryModel(){
        groups = new ArrayList<>();
        stores = new ArrayList<>();
        products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }


}
