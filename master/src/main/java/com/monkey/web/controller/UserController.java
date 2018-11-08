package com.monkey.web.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.monkey.application.Controls.IRoleService;
import com.monkey.application.Controls.IUserService;
import com.monkey.application.Controls.dtos.CreateUserInput;
import com.monkey.application.dtos.PagedAndFilterInputDto;
import com.monkey.common.base.PermissionConst;
import com.monkey.common.base.PublicResult;
import com.monkey.common.base.PublicResultConstant;
import com.monkey.common.util.ComUtil;
import com.monkey.core.dtos.UserDto;
import com.monkey.core.entity.User;
import com.monkey.web.annotation.CurrentUser;
import com.monkey.web.annotation.Log;
import com.monkey.web.controller.dtos.UserOutPut;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liugh
 * @since 2018-05-03
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    IUserService _userService;
    @Autowired
    IRoleService _roleService;

    @ApiOperation(value = "获取用户列表", notes = "用户列表")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @RequiresPermissions(value = {PermissionConst._system._user.list})
    public PublicResult<Page<User>> users(@RequestBody PagedAndFilterInputDto page) throws Exception {
        EntityWrapper<User> filter = new EntityWrapper<User>();
        filter = ComUtil.genderFilter(filter, page.where);
        Page<User> res = _userService.selectPage(new Page<>(page.index, page.size), filter);
        return new PublicResult<>(PublicResultConstant.SUCCESS, res);
    }

    @ApiOperation(value = "获取用户详情", notes = "用户列表")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions(value = {PermissionConst._system._user.first})
    public PublicResult<UserDto> user(@PathVariable Integer id) throws Exception {
        UserDto u = _userService.selectUserRole(id);
        PublicResult r = new PublicResult<>(PublicResultConstant.SUCCESS, u);
        return r;
    }

    @ApiOperation(value = "获取当前登陆用户", notes = "用户列表")
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    // @RequiresPermissions(value = {PermissionConst._user.first})
    public PublicResult<UserOutPut> user(@CurrentUser User current) throws Exception {
        UserDto u = _userService.selectUserRole(current.getId());
        if (u == null) return new PublicResult<>(PublicResultConstant.UNAUTHORIZED, null);
        User c = new User();
        c.setIsActive(u.getIsActive());
        c.setMobile(u.getMobile());
        c.setUserName(u.getUserName());
        c.setAccount(u.getAccount());
        c.setCreationTime(u.getCreationTime());
        List all = _roleService.getAllPermissions();
        Boolean canBack = false;

        UserOutPut op = new UserOutPut(c, u.getRoles(), all, u.getPermissions(), canBack);
        PublicResult r = new PublicResult<>(PublicResultConstant.SUCCESS, op);
        return r;
    }

    @Log(description = "用户接口:/添加或编辑用户")

    @ApiOperation(value = "添加或编辑用户", notes = "用户列表")
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions(value = {PermissionConst._system._user.modify})
    public PublicResult<Object> modify(@RequestBody CreateUserInput model) throws Exception {
        EntityWrapper e = new EntityWrapper();
        e.eq("account", model.account);
        if (model.id == null || model.id == 0) {
            Integer count = _userService.selectCount(e);
            if (count > 0) return new PublicResult<Object>(PublicResultConstant.FAILED, "该用户名已存在");
        }
        _userService.ModifyUserAndRoles(model);
        return new PublicResult<>(PublicResultConstant.SUCCESS, true);
    }

    @Log(description = "删除用户信息:/delete")
    @ApiOperation(value = "删除用户", notes = "用户列表")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @RequiresPermissions(value = {PermissionConst._system._user.delete})
    public PublicResult<Object> delete(@PathVariable Integer id) throws Exception {
        Boolean res = _userService.deleteById(id);
        return new PublicResult<>(PublicResultConstant.SUCCESS, res);
    }

    @Log(description = "批量删除用户:/batch")
    @ApiOperation(value = "批量删除用户", notes = "用户列表")
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    @RequiresPermissions(value = {PermissionConst._system._user.batch})
    public PublicResult<Object> batchdelete(@RequestBody List<Integer> ids) throws Exception {
        Boolean res = _userService.deleteBatchIds(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS, res);
    }
}

