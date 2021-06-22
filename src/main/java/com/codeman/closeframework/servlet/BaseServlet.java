package com.codeman.closeframework.servlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 18:55
 * @version: 1.0
 */
public class BaseServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
    }
}
