package com.ahut.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ahut.pojo.User;
import com.ahut.service.UserService;
import com.ahut.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* @author 86187
* @description 针对表【x_user】的数据库操作Service实现
* @createDate 2023-06-12 16:11:30
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public Map<String, Object> login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getUsername,user.getUsername());
        //数据库查询
        User loginUser = userMapper.selectOne(wrapper);
        //判断
        if(loginUser != null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){
            String key = "user:" + UUID.randomUUID();

            //将密码置空，存入redis，保护用户密码信息
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);

            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token",key);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> loginInfo(String token) {
        //根据token获取用户信息，用redis
        Object obj = redisTemplate.opsForValue().get(token);
        if(obj != null){
        //将token解析为user对象
        User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
        //设置data数据
        Map<String,Object> data = new HashMap<>();
        data.put("name",loginUser.getUsername());
        data.put("avatar",loginUser.getAvatar());

        //获取role数据,获取一个人所处那些职位
        List<String> roles = userMapper.getRoleByUserId(loginUser.getId());
        data.put("roles",roles);

        return data;

        }
        return null;
    }

    @Override
    public String logout(String token) {
        //在redis中将登出用户的token清除
        Boolean flag = redisTemplate.delete(token);
        if(flag){
            return "success";
        }
        return null;
    }
}




