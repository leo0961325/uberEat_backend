package com.tgfc.tw.entity.model.po;

import javax.persistence.*;

@Entity
@Table(name = "pay_detail")
public class PayDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "group_id")
    private int groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "store_id")
    private int storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "option_id")
    private int optionId;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "option_price")
    private int optionPrice;

    @Column(name = "count")
    private int count;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "is_manager_deleted", nullable = false)
    private boolean isManagerDeleted;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "pay_id", referencedColumnName = "id")
    private UserPay userPay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(int optionPrice) {
        this.optionPrice = optionPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isManagerDeleted() {
        return isManagerDeleted;
    }

    public void setManagerDeleted(boolean managerDeleted) {
        isManagerDeleted = managerDeleted;
    }

    public UserPay getUserPay() {
        return userPay;
    }

    public void setUserPay(UserPay userPay) {
        this.userPay = userPay;
    }


    public static class Builder {
        private int id;
        private int groupId;
        private String groupName;
        private int storeId;
        private String storeName;
        private int productId;
        private String productName;
        private int productPrice;
        private int optionId;
        private String optionName;
        private int optionPrice;
        private int count;
        private int totalPrice;
        private int orderId;
        private boolean isManagerDeleted;
        private UserPay userPay;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder groupId(int groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public Builder storeId(int storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder storeName(String storeName) {
            this.storeName = storeName;
            return this;
        }

        public Builder productId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder productPrice(int productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder optionId(int optionId) {
            this.optionId = optionId;
            return this;
        }

        public Builder optionName(String optionName) {
            this.optionName = optionName;
            return this;
        }

        public Builder optionPrice(int optionPrice) {
            this.optionPrice = optionPrice;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder totalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder orderId(int orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder isManagerDeleted(boolean isManagerDeleted) {
            this.isManagerDeleted = isManagerDeleted;
            return this;
        }

        public Builder userPay(UserPay userPay) {
            this.userPay = userPay;
            return this;
        }

        public PayDetail build() {
            PayDetail payDetail = new PayDetail();
            payDetail.setId(id);
            payDetail.setGroupId(groupId);
            payDetail.setGroupName(groupName);
            payDetail.setStoreId(storeId);
            payDetail.setStoreName(storeName);
            payDetail.setProductId(productId);
            payDetail.setProductName(productName);
            payDetail.setProductPrice(productPrice);
            payDetail.setOptionId(optionId);
            payDetail.setOptionName(optionName);
            payDetail.setOptionPrice(optionPrice);
            payDetail.setCount(count);
            payDetail.setTotalPrice(totalPrice);
            payDetail.setOrderId(orderId);
            payDetail.setManagerDeleted(isManagerDeleted);
            payDetail.setUserPay(userPay);
            return payDetail;
        }
    }

    @Override
    public String toString() {
        return "PayDetail{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", optionId=" + optionId +
                ", optionName='" + optionName + '\'' +
                ", optionPrice=" + optionPrice +
                ", count=" + count +
                ", totalPrice=" + totalPrice +
                ", orderId=" + orderId +
                ", isManagerDeleted=" + isManagerDeleted +
                '}';
    }
}
