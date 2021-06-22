package com.codeman.closeframework.servlet;

import com.codeman.closeframework.entity.User;
import com.codeman.closeframework.mapper.UserMapper;
import com.codeman.closeframework.util.JwtUtil;
import com.codeman.closeframework.util.MyMD5Util;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 23:49
 * @version: 1.0
 */
public class UserServlet extends BaseServlet{

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String route = req.getParameter("route");
        switch (route) {
            case "create": createUser(req, resp); break;
            default: break;
        }
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            userMapper.insert(new User().setUsername(username).setPassword(MyMD5Util.encrypt(password)));
            resp.getWriter().write("创建成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
