package com.monkey.web.controller.dtos;

import org.omg.CORBA.INTERNAL;

public class CustomerInfoDto {
    public CustomerInfoDto(){}
    public CustomerInfoDto(String openId, Integer balance, Integer order){
        this.openId=openId;
        this.balance=balance;
        this.order=order;
    }
    private   String openId;
    private  Integer balance;
    private  Integer order;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
