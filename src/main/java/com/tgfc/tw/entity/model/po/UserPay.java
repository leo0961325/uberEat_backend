package com.tgfc.tw.entity.model.po;

import javax.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_pay")
public class UserPay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "pay")
    private int pay;

    @Column(name = "pay_type")
    private int payType;

    @Column(name = "remark")
    private String remark;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "manager")
    private String manager;

    @Column(name = "debit", nullable = false)
    private boolean debit;



    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userPay")
    private List<PayDetail> payDetailList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public boolean isDebit() {
        return debit;
    }

    public void setDebit(boolean debit) {
        this.debit = debit;
    }

    public List<PayDetail> getPayDetailList() {
        return payDetailList;
    }

    public void setPayDetailList(List<PayDetail> payDetailList) {
        this.payDetailList = payDetailList;
    }


    public static class Builder {
        private int id;
        private User user;
        private boolean status;
        private int pay;
        private int payType;
        private String remark;
        private LocalDateTime date;
        private String manager;
        private boolean debit;
        private List<PayDetail> payDetailList;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder status(boolean status) {
            this.status = status;
            return this;
        }

        public Builder pay(int pay) {
            this.pay = pay;
            return this;
        }

        public Builder payType(int payType) {
            this.payType = payType;
            return this;
        }

        public Builder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder manager(String manager) {
            this.manager = manager;
            return this;
        }

        public Builder debit(boolean debit) {
            this.debit = debit;
            return this;
        }

        public Builder payDetailList(List<PayDetail> payDetailList) {
            this.payDetailList = payDetailList;
            return this;
        }

        public UserPay build() {
            UserPay userPay = new UserPay();
            userPay.setId(id);
            userPay.setUser(user);
            userPay.setStatus(status);
            userPay.setPay(pay);
            userPay.setPayType(payType);
            userPay.setRemark(remark);
            userPay.setDate(date);
            userPay.setManager(manager);
            userPay.setDebit(debit);
            userPay.setPayDetailList(payDetailList);
            return userPay;
        }
    }

    @Override
    public String toString() {
        return "UserPay{" +
                "id=" + id +
                ", user=" + user +
                ", status=" + status +
                ", pay=" + pay +
                ", payType=" + payType +
                ", remark='" + remark + '\'' +
                ", date=" + date +
                ", manager='" + manager + '\'' +
                ", debit=" + debit +
                ", payDetailList=" + payDetailList +
                '}';
    }
}

