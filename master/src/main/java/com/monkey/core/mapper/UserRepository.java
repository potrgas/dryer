package com.monkey.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.monkey.core.dtos.UserDto;
import com.monkey.core.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhaohejing123
 * @since 2018-06-05
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {

    UserDto selectUserRole(@Param("id") Integer id) ;
    List<String> selectRolePermision(@Param("roles") List<Integer> roles);
}
