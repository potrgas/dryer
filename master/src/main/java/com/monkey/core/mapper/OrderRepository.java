package com.monkey.core.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.monkey.core.dtos.*;
import com.monkey.core.entity.Order;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhaohejing
 * @since 2018-08-13
 */
public interface OrderRepository extends BaseMapper<Order> {
    void updateOrderState(@Param("num") String num, @Param("orderState") Integer orderState, @Param("payState") Integer payState, @Param("backNum") String backNum);

    TodayStatical getOrderStatical(@Param("tenantId") Integer tenantId, @Param("start") Date start, @Param("end") Date end);

    List<SalePercentDto> getTodaySalePercent(@Param("tenantId") Integer tenantId,@Param("start") Date start, @Param("end") Date end);

    List<SalePercentDto> getMonthSalePercent(@Param("tenantId") Integer tenantId,@Param("start") Date start, @Param("end") Date end);

    List<SalePercentDto> getPointSalePercent(@Param("tenantId") Integer tenantId,@Param("start") Date start, @Param("end") Date end);

    List<SalePercentDto> getPayTypePercent(@Param("tenantId") Integer tenantId,@Param("start") Date start, @Param("end") Date end);


    List<DeviceSaleStatical> getDeviceSaleStatical(@Param("tenantId") Integer tenantId, Pagination page, @Param("deviceName") String deviceName, @Param("pointName") String pointName, @Param("start") Date start, @Param("end") Date end);

    List<ProductSaleStatical> getProductSaleStatical(Integer tenantId, Pagination page,@Param("productName") String productName, @Param("deviceName") String deviceName, @Param("start") Date start, @Param("end") Date end);

}
