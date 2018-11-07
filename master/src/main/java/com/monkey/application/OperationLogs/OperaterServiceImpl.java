package com.monkey.application.OperationLogs;

import com.monkey.core.entity.Operater;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.monkey.core.mapper.OperaterRepository;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhaohejing
 * @since 2018-11-07
 */
@Service
public class OperaterServiceImpl extends ServiceImpl<OperaterRepository, Operater> implements IOperaterService {

}
