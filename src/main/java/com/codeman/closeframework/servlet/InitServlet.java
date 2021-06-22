package com.codeman.closeframework.servlet;

import com.codeman.closeframework.util.ZookeeperUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 15:51
 * @version: 1.0
 */
//@Component
@Slf4j
public class InitServlet extends BaseServlet {
    @Autowired
    private ZookeeperUtil zookeeperUtil;
    @Value("${jiaoyis.all}")
    private String jiaoyis;

    @Override
    public void init() throws ServletException {
        super.init();
        log.info("项目启动============...");
        log.info("注册本地交易...");
        String[] jiaoyis = this.jiaoyis.split(",");
//        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Stream.of(jiaoyis)
                .forEach(jiaoyi -> {
                    zookeeperUtil.createNode(jiaoyi);
                });

    }


}
