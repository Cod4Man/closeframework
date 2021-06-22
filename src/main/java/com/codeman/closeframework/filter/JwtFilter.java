package com.codeman.closeframework.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.codeman.closeframework.util.JwtUtil;
import com.codeman.closeframework.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 23:08
 * @version: 1.0
 */
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 检查是否有token，token应该是login的是否返回到客户端的
        // JWT.Header
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            // 从token从提取username
            String userName = JwtUtil.parseUserName(token);
            UserDetailsService userDetailsService = SpringContextHolder.getBean("userDetailsService");
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            // token存在，则校验token的合法性,username正确以及expiredTime没到期
            if (JwtUtil.verifyToken(token, userDetails) && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 校验合法，则给该用户授权
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }


        filterChain.doFilter(request, response);
        response.getWriter().write("敏感信息，进行过滤！");
        log.info("before response");
    }
}
