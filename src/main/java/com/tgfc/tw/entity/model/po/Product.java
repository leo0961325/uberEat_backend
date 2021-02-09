package com.tgfc.tw.entity.model.po;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "remark")
    private String remark;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    @JsonBackReference
    private Store store;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Option> optionList;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public Product(){

    }

    public Product(String name, int price, String remark, Store store){
        this.name=name;
        this.price=price;
        this.store=store;
        this.remark=remark;
    }

    public boolean canDelete(){
        List<Boolean> collect = this.getOptionList().stream().
                map(r -> r.getOrderList().stream().filter(n -> n.getUserList().size() > 0)
                        .collect(Collectors.toList()).isEmpty()).collect(Collectors.toList());
        return (collect.isEmpty() || collect.get(0));
    }
}


