package com.monkey.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.monkey.application.Controls.IUserService;
import com.monkey.application.customer.ICustomerService;
import com.monkey.common.base.Constant;
import com.monkey.common.base.PublicResult;
import com.monkey.common.base.PublicResultConstant;
import com.monkey.common.util.AesCbcUtil;
import com.monkey.common.util.ComUtil;
import com.monkey.common.util.JWTUtil;
import com.monkey.common.wechatsdk.HttpUtil;
import com.monkey.core.dtos.NgUserModel;
import com.monkey.core.entity.Customer;
import com.monkey.core.entity.User;
import com.monkey.web.annotation.Log;
import com.monkey.web.annotation.Pass;
import com.monkey.web.controller.dtos.UserLoginInput;
import com.monkey.web.controller.dtos.UserRegisterInput;
import com.monkey.web.controller.dtos.WechatCodeInput;
import io.swagger.annotations.ApiOperation;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 登录接口
 *
 * @author liugh
 * @since 2018-05-03
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private IUserService _userService;
    @Autowired
    private ICustomerService _customerService;

    @Log(description = "登录接口:/login")
    @Pass
    @ApiOperation(value = "登录接口", notes = "登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public PublicResult<NgUserModel> login(@RequestBody UserLoginInput input) throws Exception {
        if (ComUtil.isEmpty(input.userName) || ComUtil.isEmpty(input.passWord)) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.eq("account", input.userName);
        User user = _userService.selectOne(ew);
        if (ComUtil.isEmpty(user) || !BCrypt.checkpw(input.passWord, user.getPassword())) {
            return new PublicResult<>(PublicResultConstant.INVALID_USERNAME_PASSWORD, null);
        }
        user.setLastLoginTime(new Date());
        _userService.updateById(user);
        NgUserModel u = new NgUserModel(JWTUtil.sign(user.getUserName(), user.getId(), user.getPassword()), null);
        return new PublicResult<>(PublicResultConstant.SUCCESS, u);
    }

    @Log(description = "注册接口:/register")
    @Pass
    @ApiOperation(value = "注册接口", notes = "注册新用户")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public PublicResult<User> register(
            @RequestBody UserRegisterInput input,
            BindingResult error) {
        if (error.hasErrors()) {
            return new PublicResult<>(error.getAllErrors().get(0).getDefaultMessage(), null);
        }
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.where("account={0}", input.userName);
        List<User> userList = _userService.selectList(ew);
        if (!ComUtil.isEmpty(userList)) {
            return new PublicResult<>(PublicResultConstant.USERNAME_ALREADY_IN, null);
        }
        User users = new User();
        boolean result = _userService.register(users, new ArrayList<Integer>(Constant.DEFAULT_REGISTER_ROLE));
        return result ? new PublicResult<>(PublicResultConstant.SUCCESS, null) :
                new PublicResult<>(PublicResultConstant.FAILED, null);
    }

    @Pass
    @ApiOperation(value = "小程序解密", notes = "小程序")
    @RequestMapping(value = "/decode", method = RequestMethod.POST)
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
}
