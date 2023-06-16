package com.ahut.service;

import com.ahut.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
* @author 86187
* @description 针对表【x_user】的数据库操作Service
* @createDate 2023-06-12 16:11:30
*/
public interface UserService extends IService<User> {

    Map<String, Object> login(User user);

    Map<String, Object> loginInfo(String token);

    String logout( String token);
}
