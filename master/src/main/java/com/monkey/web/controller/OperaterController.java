package com.monkey.web.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.monkey.application.Device.IPointService;
import com.monkey.application.OperationLogs.IOperaterService;
import com.monkey.application.dtos.PagedAndFilterInputDto;
import com.monkey.common.base.Constant;
import com.monkey.common.base.PermissionConst;
import com.monkey.common.base.PublicResult;
import com.monkey.common.base.PublicResultConstant;
import com.monkey.common.util.ComUtil;
import com.monkey.common.util.JWTUtil;
import com.monkey.core.dtos.NgUserModel;
import com.monkey.core.entity.Operater;
import com.monkey.core.entity.Point;
import com.monkey.core.entity.User;
import com.monkey.web.annotation.Log;
import com.monkey.web.annotation.Pass;
import com.monkey.web.controller.dtos.OperaterInput;
import com.monkey.web.controller.dtos.UserLoginInput;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhaohejing
 * @since 2018-07-26
 */
@RestController
@RequestMapping("api/operater")
public class OperaterController {
    @Autowired
    IOperaterService _operaterService;

    @ApiOperation(value = "获取运维人员列表", notes = "运维人员")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @RequiresPermissions(value = {PermissionConst._operation._staff.list})
    public PublicResult<Page<Operater>> devices(@RequestBody PagedAndFilterInputDto page) throws Exception {
        EntityWrapper<Operater> filter = new EntityWrapper<>();
        filter = ComUtil.genderFilter(filter, page.where);

        Page<Operater> res = _operaterService.selectPage(new Page<>(page.index, page.size), filter);
        return new PublicResult<>(PublicResultConstant.SUCCESS, res);
    }

    @ApiOperation(value = "获取运维人员详情", notes = "运维人员")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions(value = {PermissionConst._operation._staff.first})
    public PublicResult<Operater> Point(@PathVariable Integer id) throws Exception {
        Operater m = _operaterService.selectById(id);
        return new PublicResult<>(PublicResultConstant.SUCCESS, m);
    }


    @Log(description = "点位接口:/添加或编辑运维人员")
    @ApiOperation(value = "添加或编辑运维人员", notes = "运维人员")
    @RequestMapping(method = RequestMethod.PUT)
     @RequiresPermissions(value = {PermissionConst._operation._staff.modify})
    public PublicResult<Boolean> insert(@RequestBody OperaterInput model) throws Exception {
        Operater o = new Operater();
        o.setAccount(model.account);
        o.setOpenId(model.openId);

        if (model.id == null||model.id==0) {
            String pass = BCrypt.hashpw(model.password, BCrypt.gensalt());
            o.setPassword(pass);
        }else {
            o.setId(model.id);
        }
        Boolean r = _operaterService.insertOrUpdate(o);
        return new PublicResult<>(PublicResultConstant.SUCCESS, r);
    }

    @Log(description = "运维接口:/删除运维")
    @ApiOperation(value = "删除运维", notes = "运维人员")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @RequiresPermissions(value = {PermissionConst._operation._staff.delete})
    public PublicResult<Object> delete(@PathVariable Integer id) throws Exception {
        Boolean r = _operaterService.deleteById(id);
        return new PublicResult<>(PublicResultConstant.SUCCESS, r);
    }

    @Log(description = "运维接口:/批量删除运维")
    @ApiOperation(value = "批量删除运维", notes = "运维人员")
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    @RequiresPermissions(value = {PermissionConst._operation._staff.batch})
    public PublicResult<Object> batchdelete(@RequestBody List<Integer> ids) throws Exception {
        Boolean r = _operaterService.deleteBatchIds(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS, r);
    }

    @Pass
    @Log(description = "运维接口:/登陆")
    @ApiOperation(value = "运维人员登陆", notes = "运维人员")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public PublicResult<Object> login(@RequestBody UserLoginInput input) throws Exception {
        if (ComUtil.isEmpty(input.userName) || ComUtil.isEmpty(input.passWord)) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        EntityWrapper<Operater> ew = new EntityWrapper<>();
        ew.eq("account", input.userName);
        Operater user = _operaterService.selectOne(ew);
        if (ComUtil.isEmpty(user) || !BCrypt.checkpw(input.passWord, user.getPassword())) {
            return new PublicResult<>(PublicResultConstant.INVALID_USERNAME_PASSWORD, null);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, user);
    }
}
