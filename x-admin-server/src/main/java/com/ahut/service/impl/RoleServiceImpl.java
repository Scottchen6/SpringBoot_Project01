package com.ahut.service.impl;

import com.ahut.mapper.RoleMenuMapper;
import com.ahut.pojo.RoleMenu;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ahut.pojo.Role;
import com.ahut.service.RoleService;
import com.ahut.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 86187
* @description 针对表【x_role】的数据库操作Service实现
* @createDate 2023-06-12 16:11:18
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{


    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void addRole(Role role) {
        //写入角色表
        roleMapper.insert(role);
        //写入到x_role_menu 菜单角色关系表中
        if(role.getMenuIdList() != null){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null,role.getRoleId(),menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer roleId) {
        //先依据id查询角色基本信息
        Role role = roleMapper.selectById(roleId);
        //再获取角色menuId
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(roleId);
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        //先修改x_role表
        roleMapper.updateById(role);
        //再修改x_role_menu表，删除该用户的旧权限，加上新权限
        roleMenuMapper.deleteById(role.getRoleId());
        //加上新权限
        if(role.getMenuIdList() != null){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null,role.getRoleId(),menuId));
            }
        }

    }

    @Override
    public void removeRole(Integer roleId) {
        roleMapper.deleteById(roleId);
        roleMenuMapper.deleteByRoleId(roleId);
    }
}




