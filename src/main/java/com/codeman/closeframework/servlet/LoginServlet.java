package com.codeman.closeframework.servlet;

import com.codeman.closeframework.util.JwtUtil;
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
public class LoginServlet extends BaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            String token = JwtUtil.geneToken(username, password);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
