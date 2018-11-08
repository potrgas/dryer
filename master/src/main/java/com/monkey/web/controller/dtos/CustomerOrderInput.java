package com.monkey.web.controller.dtos;

public class CustomerOrderInput {
    /**
     * 客户id
     */
    public String openId;
    /**
     * 烘干类型(纯棉 化纤 其他)
     */
    public Integer dryType;
    /**
     * 客户姓名
     */
    public String customerName;
    /**
     * 手机
     */
    public String mobile;
    /**
     * 地区
     */
    public String area;
    /**
     * 小区
     */
    public String community;
    /**
     * 取件地址
     */
    public String address;
    /**
     * 是否次数支付
     */
    public Integer isTime;
    /**
     * 价格
     */
    public Integer price;
}
