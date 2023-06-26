package com.ahut;

import com.ahut.mapper.MenuMapper;
import com.ahut.pojo.Menu;
import com.ahut.pojo.User;
import com.ahut.utils.JwtUtil;
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


    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void contextLoads2() {
        User user = new User();
        user.setUsername("chenlei666");
        user.setPhone("12345678901");
        String token = jwtUtil.createToken(user);
        System.out.println(token);
        System.out.println("=========================");
        System.out.println(jwtUtil.parseToken(token, User.class));
        System.out.println("+++++++++++++++++++++++++++");
        System.out.println(jwtUtil.parseToken(token));
    }
}
