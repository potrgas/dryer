package com.monkey.application.Payfor;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.monkey.core.entity.Chargeorder;
import com.monkey.core.mapper.ChargeorderRepository;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhaohejing
 * @since 2018-11-06
 */
@Service
public class ChargeorderServiceImpl extends ServiceImpl<ChargeorderRepository, Chargeorder> implements IChargeorderService {

}
