package com.ahut.mapper;

import com.ahut.pojo.Role;
import com.ahut.pojo.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;

/**
* @author 86187
* @description 针对表【x_role_menu】的数据库操作Mapper
* @createDate 2023-06-12 16:11:22
* @Entity com.ahut.pojo.RoleMenu
*/
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Integer> getMenuIdListByRoleId(Integer roleId);

    void deleteByRoleId(Integer roleId);
}




