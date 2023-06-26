package com.ahut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ahut.pojo.Menu;
import com.ahut.service.MenuService;
import com.ahut.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.List;

/**
* @author 86187
* @description 针对表【x_menu】的数据库操作Service实现
* @createDate 2023-06-12 16:04:38
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Override
    public List<Menu> getAllMenu() {
        //获取一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,0);
        List<Menu> menuList = this.list(wrapper);
        //填充子菜单
        setMenuChildren(menuList);
        return menuList;
    }

    /**
     * 抽取出方法，递归循环获取子菜单
     * @param menuList
     */
    private void setMenuChildren(List<Menu> menuList) {
        if(menuList != null){
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Menu::getParentId,menu.getMenuId());
                List<Menu> childrenList = this.list(queryWrapper);
                menu.setChildren(childrenList);
                setMenuChildren(childrenList);
            }
        }
    }

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        //先获取一级菜单,一级菜单的parentId为0
        List<Menu> menuList = menuMapper.getMenuListByUserId(userId, 0);
        //调用以下方法获取子菜单
        setMenuChildrenByUserId(userId, menuList);
        return menuList;
    }

    /**
     * 抽取出函数，循环递归获取子菜单
     * @param userId
     * @param menuList
     */
    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if(menuList != null){
            for (Menu menu : menuList) {
                List<Menu> childrenMenuList = menuMapper.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(childrenMenuList);
                setMenuChildrenByUserId(userId,childrenMenuList);
            }
        }
    }

}




