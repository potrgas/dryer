package com.monkey.application.customer;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.monkey.core.entity.Customer;
import com.monkey.core.mapper.CustomerRepository;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhaohejing
 * @since 2018-10-30
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerRepository, Customer> implements ICustomerService {

}
