package com.monkey.core.entity;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhaohejing
 * @since 2018-11-08
 */
@TableName("sale_customerOrder")
public class CustomerOrder extends Model<CustomerOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    /**
     * guid
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 客户id
     */
    private String openId;
    /**
     * 烘干类型(纯棉 化纤 其他)
     */
    private Integer dryType;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 地区
     */
    private String area;
    /**
     * 小区
     */
    private String community;
    /**
     * 取件地址
     */
    private String address;
    /**
     * 订单状态
     */
    private Integer orderState;
    /**
     * 支付状态
     */
    private Integer payState;
    /**
     * 设备id
     */
    private Integer operaterId;
    /**
     * 是否次数支付
     */
    private Integer isTime;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date creationTime;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer creatorUserId;
    /**
     * 价格
     */
    private Integer price;
    /**
     * 对接订单编号
     */
    private String orderNum;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getDryType() {
        return dryType;
    }

    public void setDryType(Integer dryType) {
        this.dryType = dryType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Integer getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Integer operaterId) {
        this.operaterId = operaterId;
    }

    public Integer getIsTime() {
        return isTime;
    }

    public void setIsTime(Integer isTime) {
        this.isTime = isTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Integer creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
        "id=" + id +
        ", openId=" + openId +
        ", dryType=" + dryType +
        ", customerName=" + customerName +
        ", mobile=" + mobile +
        ", area=" + area +
        ", community=" + community +
        ", address=" + address +
        ", orderState=" + orderState +
        ", payState=" + payState +
        ", operaterId=" + operaterId +
        ", isTime=" + isTime +
        ", creationTime=" + creationTime +
        ", creatorUserId=" + creatorUserId +
        ", price=" + price +
        ", orderNum=" + orderNum +
        "}";
    }
}
