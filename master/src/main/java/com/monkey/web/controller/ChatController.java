package com.monkey.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.monkey.application.Payfor.IChargeorderService;
import com.monkey.application.Payfor.ICustomerOrderService;
import com.monkey.application.Payfor.IOrderService;
import com.monkey.application.customer.ICustomerService;
import com.monkey.application.dtos.PagedAndFilterInputDto;
import com.monkey.common.base.Constant;
import com.monkey.common.base.PublicResult;
import com.monkey.common.base.PublicResultConstant;
import com.monkey.common.util.AesCbcUtil;
import com.monkey.common.wechatsdk.HttpUtil;
import com.monkey.common.wechatsdk.PayConfig;
import com.monkey.core.entity.Chargeorder;
import com.monkey.core.entity.Customer;
import com.monkey.core.entity.CustomerOrder;
import com.monkey.core.entity.Order;
import com.monkey.web.annotation.Log;
import com.monkey.web.annotation.Pass;
import com.monkey.web.controller.dtos.*;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.SortedMap;
import java.util.TreeMap;


/**
 * @author liugh
 * @since 2018-05-03
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private ICustomerService _customerService;
    @Autowired
    ICustomerOrderService _customerOrderService;
    @Autowired
    IChargeorderService _chargeOrderService;
    @Pass
    @ApiOperation(value = "小程序解密", notes = "小程序")
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public PublicResult<Customer> customer(@RequestBody WechatCodeInput input) {
        //登录凭证不能为空
        if (input.getCode() == null || input.getCode().length() == 0) {
            return new PublicResult<Customer>(PublicResultConstant.INVALID_TENANT_NAME, null);
        }
        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
        //请求参数
        String params = "appid=" + PayConfig.MP_CUSTOMER_APPID + "&secret=" + PayConfig.MP_CUSTOMER_SECRET + "&js_code=" + input.getCode() + "&grant_type=" + Constant.grant_type;
        //发送请求
        String sr = HttpUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sr);
        //获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        //用户的唯一标识（openid）
        String openid = (String) json.get("openid");
        EntityWrapper ew = new EntityWrapper();
        ew.eq("openId", openid);
        Customer c = _customerService.selectOne(ew);
        if (input.getEncryptedData().isEmpty() || input.getIv().isEmpty()) {
            if (c == null) {
                c = new Customer();
                c.setOpenId(openid);
                c.setBalance(0);
                _customerService.insert(c);
            }
            return new PublicResult<Customer>(PublicResultConstant.SUCCESS, c);
        }
        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
        try {
            String result = AesCbcUtil.decrypt(input.getEncryptedData(), session_key, input.getIv(), "UTF-8");
            if (null != result && result.length() > 0) {
                JSONObject userInfoJSON = JSONObject.parseObject(result);
                if (c == null) {
                    c = new Customer();
                    c.setBalance(0);
                    c.setNickName(userInfoJSON.get("nickName").toString());
                    c.setOpenId(userInfoJSON.get("openId").toString());
                    c.setAddress(userInfoJSON.get("country") + userInfoJSON.get("province").toString() + userInfoJSON.get("city"));
                    c.setMobile("");
                    c.setAvatarUrl(userInfoJSON.get("avatarUrl").toString());
                    c.setUnionId(userInfoJSON.get("unionId").toString());
                    _customerService.insert(c);
                }
                return new PublicResult<Customer>(PublicResultConstant.SUCCESS, c);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new PublicResult<Customer>(PublicResultConstant.FAILED, null);
    }

    @Pass
    @Log(description = "小程序:/客户下单操作")
    @ApiOperation(value = "客户下单操作", notes = "小程序")
    @RequestMapping(value = "/make", method = RequestMethod.POST)
    public PublicResult<Object> insert(@RequestBody CustomerOrderInput model) throws Exception {
        try {
            CustomerOrder r = _customerOrderService.insertOrder(model);
            if (!r.getId().isEmpty()) {
                SortedMap<String, Object> result;
                result = _customerOrderService.wxPay(r);
                if (!result.isEmpty()) {
                    return new PublicResult<>(PublicResultConstant.SUCCESS, result);
                }
            }
            return new PublicResult<>(PublicResultConstant.ERROR, r);
        } catch (Exception e) {
            return new PublicResult<>(PublicResultConstant.FAILED, e.getMessage());
        }
    }


    @Pass
    @Log(description = "小程序:/客户充值操作")
    @ApiOperation(value = "客户充值操作", notes = "小程序")
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public PublicResult<Object> charge(@RequestBody ChargeOrderInput model) throws Exception {
        try {
            Chargeorder r = _chargeOrderService.insertChargeOrder(model);
            if (!r.getId().isEmpty()) {
                SortedMap<String, Object> result;
                result = _chargeOrderService.wxChargePay(r);
                if (!result.isEmpty()) {
                    return new PublicResult<>(PublicResultConstant.SUCCESS, result);
                }
                return new PublicResult<>(PublicResultConstant.ERROR, r);
            }
            return new PublicResult<>(PublicResultConstant.ERROR, r);
        } catch (Exception e) {
            return new PublicResult<>(PublicResultConstant.FAILED, e.getMessage());
        }
    }

    @Pass
    @ApiOperation(value = "获取订单和余额", notes = "小程序")
    @RequestMapping(value = "/info/{openId}", method = RequestMethod.GET)
    public PublicResult<CustomerInfoDto> back(@PathVariable String openId) throws Exception {
        try {
            CustomerInfoDto res = new CustomerInfoDto(openId, 0, 0);
            EntityWrapper ew = new EntityWrapper();
            ew.eq("openId", openId);
            Customer r = _customerService.selectOne(ew);
            if (r != null) {
                Integer count = _customerOrderService.selectCount(ew);
                res.setBalance(r.getBalance());
                res.setOrder(count);
                return new PublicResult<>(PublicResultConstant.SUCCESS, res);
            }
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        } catch (Exception e) {
            return new PublicResult<>(PublicResultConstant.FAILED, null);
        }
    }

    @Pass
    @ApiOperation(value = "获取订单列表", notes = "小程序")
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public PublicResult<Page<CustomerOrder>> orders(@RequestBody PagedAndFilterInputDto page) throws Exception {
        EntityWrapper<CustomerOrder> filter = new EntityWrapper<>();
        String openId = (String) page.where.get("openId");
        filter.eq("openId", openId);
        Page<CustomerOrder> res = _customerOrderService.selectPage(new Page<>(page.index, page.size), filter);
        return new PublicResult<>(PublicResultConstant.SUCCESS, res);
    }

    @Pass
    @ApiOperation(value = "获取订单详情", notes = "小程序")
    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    public PublicResult<CustomerOrder> order(@PathVariable String id) throws Exception {
        CustomerOrder o= _customerOrderService.selectById(id);
       if(o==null)return  new PublicResult<CustomerOrder>(PublicResultConstant.FAILED,null);
        return new PublicResult<>(PublicResultConstant.SUCCESS, o);
    }
    @Pass
    @ApiOperation(value = "更新订单进度", notes = "小程序")
    @RequestMapping(value = "/order/{id}/{state}", method = RequestMethod.PUT)
    public PublicResult<CustomerOrder> update(@PathVariable String id,@PathVariable Integer state) throws Exception {
        CustomerOrder o= _customerOrderService.selectById(id);
        if(o==null)return  new PublicResult<CustomerOrder>(PublicResultConstant.FAILED,null);
        o.setOrderState(state);
        _customerOrderService.updateById(o);
        return new PublicResult<>(PublicResultConstant.SUCCESS, o);
    }
}
