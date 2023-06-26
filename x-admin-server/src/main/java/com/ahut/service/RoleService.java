package com.ahut.service;

import com.ahut.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86187
* @description 针对表【x_role】的数据库操作Service
* @createDate 2023-06-12 16:11:18
*/
public interface RoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer roleId);

    void updateRole(Role role);

    void removeRole(Integer roleId);

}
