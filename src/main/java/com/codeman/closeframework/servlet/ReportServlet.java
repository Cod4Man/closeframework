package com.codeman.closeframework.servlet;

import com.alibaba.fastjson.JSON;
import com.codeman.closeframework.http.RemoteCall;
import com.codeman.closeframework.util.SpringContextHolder;
import com.codeman.closeframework.vo.CommonInVO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 14:29
 * @version: 1.0
 */
@Component
@Slf4j
public class ReportServlet extends BaseServlet {



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jiaoyi = req.getParameter("jiaoyi");
//        RemoteCall.call(jiaoyi, );
        try {
            Object bean = SpringContextHolder.getBean(jiaoyi + "Service");
//            Class<?> aClass = Class.forName("com.codeman.closeframework.controller." + jiaoyi + "Service");
            Method method = bean.getClass().getMethod(jiaoyi.substring(0, 1).toLowerCase() + jiaoyi.substring(1), HttpServletRequest.class);
            Object result = method.invoke(bean, req);
            log.info("请求交易【" + jiaoyi + "】成功，响应：" + result.toString());
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(result.toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
