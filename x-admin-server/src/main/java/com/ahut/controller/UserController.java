package com.ahut.controller;

import com.ahut.pojo.User;
import com.ahut.service.UserService;
import com.ahut.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.management.ObjectName;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : Scott Chen
 * @create 2023/6/12 17:10
 */

@RestController
@RequestMapping("/user")

//解决跨域问题的注解
@CrossOrigin

public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user){ //标注请求体注解，解析json数据为user对象
        //获取data值
        Map<String,Object> data = userService.login(user);
        if(data != null){
            return Result.success(data);
        }else{
            return Result.fail(20002,"用户名或密码错误！");
        }
    }


    /**
     * 获取登录用户信息
     * @param token
     * @return
     */
    @GetMapping("/info")
    public Result<Map<String,Object>> loginInfo(@RequestParam("token") String token){ //标注请求参数注解
        //获取data值,根据token获取用户信息，用redis
        Map<String,Object> data = userService.loginInfo(token);

        if(data != null){
            return Result.success(data);
        }else{
            return Result.fail(20003,"获取信息失败，请重新登录！");
        }
    }


    /**
     * 用户退出
     * @param token
     * @return
     */
    @PostMapping("/logout")
    public Result<Map<String,Object>> logout(@RequestHeader("X-Token") String token){ //X-Token是请求头里面的 一直携带token数据
        String data = userService.logout(token);
        if(data != null){
            return Result.success(data);
        }
        return Result.fail(20004,"已退出，请勿重复操作！");
    }


    @GetMapping("/list")
    public Result<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                                  @RequestParam(value = "phone",required = false) String phone,
                                                  @RequestParam long pageNo,
                                                  @RequestParam long pageSize){
        //查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!StringUtil.isNullOrEmpty(username),User::getUsername,username)
                .eq(!StringUtil.isNullOrEmpty(phone),User::getPhone,phone);
        //分页条件
        Page<User> page = new Page<>(pageNo,pageSize);
        //依据查询条件得出的page数据
        Page<User> userPage = userService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        //获取总记录数
        data.put("total",userPage.getTotal());
        //获取符合查询条件的全部用户信息userList
        data.put("rows",userPage.getRecords());

        return Result.success(data);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping
    public Result<Map<String,Object>> addUser(@RequestBody User user){ //标注请求体注解，解析json数据为user对象
        //使用MD5算法加密用户密码
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userService.save(user);
        return Result.success("新增用户成功！");
    }


    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PutMapping
    public  Result<?> updateUser(@RequestBody User user){
        user.setPassword(null);
        userService.updateById(user);
        return Result.success("修改用户信息成功");
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<?> getUserById(@PathVariable("id") Integer id){
        User user = userService.getById(id);
        return Result.success(user);

    }


    /**
     * 根据id删除用户信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable Integer id){
        userService.removeById(id);
        return  Result.success("删除用户成功");
    }


}
