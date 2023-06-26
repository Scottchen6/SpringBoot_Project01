package com.ahut.service.impl;

import com.ahut.mapper.RoleMapper;
import com.ahut.mapper.UserRoleMapper;
import com.ahut.pojo.Menu;
import com.ahut.pojo.Role;
import com.ahut.pojo.UserRole;
import com.ahut.service.MenuService;
import com.ahut.utils.JwtUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ahut.pojo.User;
import com.ahut.service.UserService;
import com.ahut.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.core.support.PersistentEntityInformation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 86187
 * @description 针对表【x_user】的数据库操作Service实现
 * @createDate 2023-06-12 16:11:30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Map<String, Object> login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getUsername, user.getUsername());
        //数据库查询
        User loginUser = userMapper.selectOne(wrapper);
        //判断
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            //UUID 最终是要用jwt的
            //String key = "user:" + UUID.randomUUID();

            //将密码置空,保护用户密码信息
            loginUser.setPassword(null);
            String token = jwtUtil.createToken(loginUser);

            //将密码置空，存入redis，保护用户密码信息
            //loginUser.setPassword(null);
            //redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);

            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return data;
        }
        return null;
    }

    @Resource
    private MenuService menuService;

    @Override
    public Map<String, Object> loginInfo(String token) {
        //根据token获取用户信息，用redis
        //Object obj = redisTemplate.opsForValue().get(token);

        //根据jwt给工具获取token
        User objUser = null;
        try {
            objUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (objUser != null) {
            //将token解析为user对象
            User loginUser = JSON.parseObject(JSON.toJSONString(objUser), User.class);
            //设置data数据
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());

            //获取role数据,获取一个人所处那些职位(角色)
            List<String> roles = userMapper.getRoleByUserId(loginUser.getId());
            data.put("roles", roles);

            //权限列表，展示该用户的所有权限
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList",menuList);

            return data;

        }
        return null;
    }

    @Override
    public String logout(String token) {
        //在redis中将登出用户的token清除
       /* Boolean flag = redisTemplate.delete(token);
        if(flag){
            return "success";
        }
        return null;*/

        return "success";
    }

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 添加用户和其对应角色身份（分别再两个表）
     *
     * @param user
     */
    @Override
    public void addUser(User user) {
        //先添加啊用户表
        userMapper.insert(user);
        //再添加到用户角色表
        if (user.getRoleIdList() != null) {
            for (Integer roleId : user.getRoleIdList()) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }


    /**
     * 工具id查询用户信息，再对话框中回显信息（信息和权限分别再两个表）
     *
     * @param id
     * @return
     */
    @Override
    public User getUserById(Integer id) {
        //查询用户基本信息，同此前
        User user = userMapper.selectById(id);
        //再查询用户权限
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoleList = userRoleMapper.selectList(wrapper);
        //从userRoleList中获取roleId的集合roleIdList
        List<Integer> roleIdList = userRoleList.stream()
                .map(userRole -> {
                    return userRole.getRoleId();
                }).collect(Collectors.toList());
        //将roleIdList存入user对象中
        user.setRoleIdList(roleIdList);
        return user;
    }

    /**
     * 修改用户信息和用户权限（分别再两个表）
     *
     * @param user
     */
    @Override
    public void updateUser(User user) {
        //先修改用户信息
        userMapper.updateById(user);
        //再修改用户权限
        //删除原有的权限
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, user.getId());
        userRoleMapper.delete(wrapper);
        //添加新的权限
        if (user.getRoleIdList() != null) {
            for (Integer roleId : user.getRoleIdList()) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    /**
     * 根据id删除用户信息和权限（分别再两个表）
     *
     * @param id
     */
    @Override
    public void removeUser(Integer id) {
        //删除用户信息（user表）
        userMapper.deleteById(id);
        //删除用户权限（user_role表）
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(wrapper);
    }
}




