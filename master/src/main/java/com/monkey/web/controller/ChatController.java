package com.monkey.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.monkey.application.Payfor.IOrderService;
import com.monkey.application.customer.ICustomerService;
import com.monkey.application.dtos.PagedAndFilterInputDto;
import com.monkey.common.base.Constant;
import com.monkey.common.base.PermissionConst;
import com.monkey.common.base.PublicResult;
import com.monkey.common.base.PublicResultConstant;
import com.monkey.common.util.AesCbcUtil;
import com.monkey.common.util.ComUtil;
import com.monkey.common.wechatsdk.HttpUtil;
import com.monkey.core.entity.Chargeorder;
import com.monkey.core.entity.Customer;
import com.monkey.core.entity.Order;
import com.monkey.web.annotation.Log;
import com.monkey.web.annotation.Pass;
import com.monkey.web.controller.dtos.ChargeOrderInput;
import com.monkey.web.controller.dtos.CustomerInfoDto;
import com.monkey.web.controller.dtos.OrderInput;
import com.monkey.web.controller.dtos.WechatCodeInput;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    IOrderService _orderService;

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
        String params = "appid=" + Constant.wxspAppid + "&secret=" + Constant.wxspSecret + "&js_code=" + input.getCode() + "&grant_type=" + Constant.grant_type;
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
    @Log(description = "订单接口:/客户下单操作")
    @ApiOperation(value = "客户下单操作", notes = "小程序")
    @RequestMapping(value = "/make", method = RequestMethod.POST)
    public PublicResult<Object> insert(@RequestBody OrderInput model) throws Exception {
        try {
            Order r = _orderService.insertOrder(model);
            if (!r.getId().isEmpty()) {
                SortedMap<String, Object> result = new TreeMap<>();
                if (model.payType == 1) {
                    result = _orderService.wxPay(r);
                } else {
                }
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
    @Log(description = "订单接口:/客户充值操作")
    @ApiOperation(value = "客户充值操作", notes = "小程序")
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public PublicResult<Object> charge(@RequestBody ChargeOrderInput model) throws Exception {
        try {
            Chargeorder r = _orderService.insertChargeOrder(model);
            if (!r.getId().isEmpty()) {
                SortedMap<String, Object> result;
                result = _orderService.wxChargePay(r);
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
    @Log(description = "获取余额和订单:/")
    @ApiOperation(value = "获取订单和余额", notes = "小程序")
    @RequestMapping(value = "/info/{openId}", method = RequestMethod.GET)
    public PublicResult<CustomerInfoDto> back(@PathVariable String openId) throws Exception {
        try {
            CustomerInfoDto res = new CustomerInfoDto(openId, 0, 0);
            EntityWrapper ew = new EntityWrapper();
            ew.eq("openId", openId);
            Customer r = _customerService.selectOne(ew);
            if (r != null) {
                Integer count = _orderService.selectCount(ew);
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
    public PublicResult<Page<Order>> orders(@RequestBody PagedAndFilterInputDto page) throws Exception {
        EntityWrapper<Order> filter = new EntityWrapper<>();
        String openId = (String) page.where.get("openId");
        filter.eq("openId", openId);
        Page<Order> res = _orderService.selectPage(new Page<>(page.index, page.size), filter);
        return new PublicResult<>(PublicResultConstant.SUCCESS, res);
    }
}
