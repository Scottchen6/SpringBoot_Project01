package com.ahut.controller;

import com.ahut.pojo.Role;
import com.ahut.service.RoleService;
import com.ahut.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : Scott Chen
 * @create 2023/6/17 15:37
 */

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public Result<Map<String,Object>> getRoleList(  @RequestParam(value = "roleName",required = false) String roleName,
                                                    @RequestParam long pageNo,
                                                    @RequestParam long pageSize){

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!StringUtil.isNullOrEmpty(roleName),Role::getRoleDesc,roleName);
        //分页
        Page<Role> page = new Page<>(pageNo, pageSize);
        //添加分页时候的查询条件
        Page<Role> rolePage = roleService.page(page, wrapper);

        Map<String,Object> data = new HashMap<>();

        data.put("rows",rolePage.getRecords());
        data.put("total",rolePage.getTotal());

        return Result.success(data);
    }

    @PostMapping
    public Result<?> addRole(@RequestBody Role role){ //@RequstBody注解使得传过来的json数据转化为role对象
        //写入角色表，并确定该角色的权限，写入到x_role_menu表中
        roleService.addRole(role);
        return Result.success("添加角色成功");
    }

    @GetMapping("/{roleId}")
    public Result<?> getRoleById(@PathVariable Integer roleId){
        Role role = roleService.getRoleById(roleId);
        return Result.success(role);
    }

    @PutMapping
    public Result<?> updateRole(@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success("修改角色信息成功！");
    }


    @DeleteMapping("/{roleId}")
    public Result<?> deleteRole(@PathVariable Integer roleId){
        roleService.removeRole(roleId);
        return Result.success("删除角色成功！");
    }

    @GetMapping("/allRole")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }



}
