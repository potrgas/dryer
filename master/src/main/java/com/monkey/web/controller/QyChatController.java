package com.monkey.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
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
import com.monkey.core.entity.Order;
import com.monkey.web.annotation.Log;
import com.monkey.web.annotation.Pass;
import com.monkey.web.controller.dtos.ChargeOrderInput;
import com.monkey.web.controller.dtos.CustomerInfoDto;
import com.monkey.web.controller.dtos.OrderInput;
import com.monkey.web.controller.dtos.WechatCodeInput;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/qy")
public class QyChatController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private ICustomerService _customerService;
    @Autowired
    IOrderService _orderService;

    @Pass
    @Log(description = "企业微信:/客户充值操作")
    @ApiOperation(value = "用户同步", notes = "企业微信")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public PublicResult<Object> genderUser() throws Exception {
        EntityWrapper ew = new EntityWrapper();
        List<String> ul = getUserList();
        List<Customer> customers = _orderService.selectList(ew);
        if (ul != null && !ul.isEmpty()) {
            for (String u :
                    ul) {

            }

        }
        return  new PublicResult<Object>(PublicResultConstant.SUCCESS,null);
    }

    /// <summary>
    /// 获取机构人员
    /// </summary>
    /// <returns></returns>
    private List<String> getUserList() {
        String token = GetAccessToken();
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=" + token + "&department_id=1&fetch_child=1&status=0";
        String result = HttpUtil.sendGet(url, "");
        JSONObject json = JSONObject.parseObject(result);
        if (json.get("userlist") != null) {
            Object ticket = json.get("userlist");

        }
        return null;
    }

    /// <summary>
    /// 获取accesstoken
    /// </summary>
    /// <returns></returns>
    private static String GetAccessToken() {
        String url =
                "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + PayConfig.QY_CORPID + "&corpsecret=" + PayConfig.QY_SECRETA;
        String result = HttpUtil.sendGet(url, "");
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(result);
        //获取会话密钥（session_key）
        String session_key = json.get("access_token").toString();
        return session_key;
    }
}
