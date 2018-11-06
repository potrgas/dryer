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
 * @since 2018-11-06
 */
@TableName("sale_chargeorder")
public class Chargeorder extends Model<Chargeorder> {

    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    /**
     * guid
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    private String openId;
    /**
     * 订单状态
     */
    private Integer orderState;
    private Integer count;
    /**
     * 支付状态
     */
    private Integer payState;
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
     * 微信订单
     */
    private String wechatOrder;
    /**
     * 商品名
     */
    private String productName;


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

    public String getWechatOrder() {
        return wechatOrder;
    }

    public void setWechatOrder(String wechatOrder) {
        this.wechatOrder = wechatOrder;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Chargeorder{" +
        "id=" + id +
        ", openId=" + openId +
        ", orderState=" + orderState +
        ", payState=" + payState +
        ", creationTime=" + creationTime +
        ", creatorUserId=" + creatorUserId +
        ", price=" + price +
        ", wechatOrder=" + wechatOrder +
        ", productName=" + productName +
        "}";
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
