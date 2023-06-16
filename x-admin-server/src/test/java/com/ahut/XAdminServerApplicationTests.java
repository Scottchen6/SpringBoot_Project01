package com.ahut;

import com.ahut.mapper.MenuMapper;
import com.ahut.pojo.Menu;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@MapperScan("com.ahut.mapper")
class XAdminServerApplicationTests {

    @Autowired
    private MenuMapper menuMapper;

    @Test
    void contextLoads() {
        List<Menu> menus = menuMapper.selectList(null);
        System.out.println(menus);
    }

}
