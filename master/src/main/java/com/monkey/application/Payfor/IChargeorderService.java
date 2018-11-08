package com.monkey.application.Payfor;

import com.baomidou.mybatisplus.service.IService;
import com.monkey.core.entity.Chargeorder;
import com.monkey.web.controller.dtos.ChargeOrderInput;

import java.util.SortedMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhaohejing
 * @since 2018-11-06
 */
public interface IChargeorderService extends IService<Chargeorder> {
    Chargeorder insertChargeOrder(ChargeOrderInput input) throws Exception;
    SortedMap<String, Object> wxChargePay(Chargeorder input);


}
