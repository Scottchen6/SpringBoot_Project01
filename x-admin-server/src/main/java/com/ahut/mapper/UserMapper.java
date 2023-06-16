package com.ahut.mapper;

import com.ahut.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86187
* @description 针对表【x_user】的数据库操作Mapper
* @createDate 2023-06-12 16:11:30
* @Entity com.ahut.pojo.User
*/
public interface UserMapper extends BaseMapper<User> {

    List<String> getRoleByUserId(Integer userId);

}




