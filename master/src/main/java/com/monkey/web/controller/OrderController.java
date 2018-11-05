package com.monkey.web.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.monkey.application.Device.IPointService;
import com.monkey.application.Payfor.IOrderService;
import com.monkey.application.dtos.PagedAndFilterInputDto;
import com.monkey.common.base.Constant;
import com.monkey.common.base.PermissionConst;
import com.monkey.common.base.PublicResult;
import com.monkey.common.base.PublicResultConstant;
import com.monkey.common.util.ComUtil;
import com.monkey.common.wechatsdk.QrCodeUtil;
import com.monkey.core.entity.Order;
import com.monkey.web.annotation.Log;
import com.monkey.web.controller.dtos.OrderInput;
import com.monkey.web.controller.dtos.RequestDateDto;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhaohejing
 * @since 2018-07-26
 */
@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    IOrderService _orderService;
    @Autowired
    IPointService _pointService;

    @ApiOperation(value = "获取订单列表", notes = "订单列表")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @RequiresPermissions(value = {PermissionConst._orders._order.list})
    public PublicResult<Page<Order>> orders(@RequestBody PagedAndFilterInputDto page) throws Exception {
        EntityWrapper<Order> filter = new EntityWrapper<>();
        filter = ComUtil.genderFilter(filter, page.where);
        String code = (String) page.where.get("code");
        if (code != null && !code.isEmpty()) {
            if(code.equals(Constant.UnknownCode)){
                filter.where("pointName is null").or("pointName=''");
            }else{
                List<String> ids= _pointService.selectPointNameByCode(code);
                if(!ids.isEmpty()){
                    filter.in("pointName",ids);
                }
            }
        }
        Page<Order> res = _orderService.selectPage(new Page<>(page.index, page.size), filter);
        return new PublicResult<>(PublicResultConstant.SUCCESS, res);
    }

    @ApiOperation(value = "获取订单详情", notes = "订单列表")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions(value = {PermissionConst._orders._order.list})
    public PublicResult<Order> Product(@PathVariable Integer id) throws Exception {
        Order m = _orderService.selectById(id);
        return new PublicResult<>(PublicResultConstant.SUCCESS, m);
    }



}

