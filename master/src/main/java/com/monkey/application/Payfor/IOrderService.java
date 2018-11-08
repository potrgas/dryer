package com.monkey.application.Payfor;

import com.baomidou.mybatisplus.plugins.Page;
import com.monkey.core.dtos.DeviceSaleStatical;
import com.monkey.core.dtos.ProductSaleStatical;
import com.monkey.core.entity.Chargeorder;
import com.monkey.core.entity.Order;
import com.baomidou.mybatisplus.service.IService;
import com.monkey.web.controller.dtos.ChargeOrderInput;
import com.monkey.web.controller.dtos.OrderInput;
import com.monkey.web.controller.dtos.StaticalInput;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhaohejing
 * @since 2018-08-13
 */
public interface IOrderService extends IService<Order> {
    Order insertOrder(OrderInput input) throws Exception;
    Chargeorder insertChargeOrder(ChargeOrderInput input) throws Exception;

    String weixinPay(Order input) throws Exception;

    String aliPay(Order input) throws Exception;

    void updateOrderStatte(String orderNum, Integer orderState, Integer payState,String backNum);
    SortedMap<String, Object> wxPay(Order input);
    SortedMap<String, Object> wxChargePay(Chargeorder input);
    String weixinBack(Order input) throws  Exception;
    Map<String,Object> getDashboard(Integer tenantId);
    Map<String,Object> getStaticial(Integer tenantId, Date start,Date end);
    String  aliback(Order input)throws  Exception;
    Page<DeviceSaleStatical>  getDeviceSaleStatical(Page<DeviceSaleStatical> page, StaticalInput input );
    Page<ProductSaleStatical>  getProductSaleStatical(Page<ProductSaleStatical> page, StaticalInput input );
}
