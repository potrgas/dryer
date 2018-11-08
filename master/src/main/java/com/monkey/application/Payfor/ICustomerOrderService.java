package com.monkey.application.Payfor;

import com.baomidou.mybatisplus.service.IService;
import com.monkey.core.entity.CustomerOrder;
import com.monkey.web.controller.dtos.CustomerOrderInput;

import java.util.SortedMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhaohejing
 * @since 2018-11-08
 */
public interface ICustomerOrderService extends IService<CustomerOrder> {

    CustomerOrder insertOrder(CustomerOrderInput input) throws Exception;

    SortedMap<String, Object> wxPay(CustomerOrder input);
    String weixinBack(CustomerOrder input) throws  Exception;
}
