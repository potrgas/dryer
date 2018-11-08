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

   
     /// <summary>
        /// 获取accesstoken
        /// </summary>
        /// <returns></returns>
        private static String  GetAccessToken()
        {
            String url =
              "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={CorpId}&corpsecret={SecretA}";
            String result =  HttpUtil.sendGet(url,"");
               //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(result);
        //获取会话密钥（session_key）
        String session_key = json.get("access_token").toString();
            return session_key;
        }
}
