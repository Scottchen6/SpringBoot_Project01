package com.ahut.mapper;

import com.ahut.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86187
* @description 针对表【x_menu】的数据库操作Mapper
* @createDate 2023-06-12 16:04:38
* @Entity com.ahut.pojo.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuListByUserId(@Param("userId") Integer userId,@Param("parentId") Integer parentId);
}




