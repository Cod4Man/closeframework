package com.codeman.closeframework.util;

import com.codeman.closeframework.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 22:29
 * @version: 1.0
 */
//@Component
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtFilter jwtFilter;

    @Override
    // 身份认证
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
//        super.configure(auth);
    }

    @Override
    // 权限认证
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 认证失败处理类
                //.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session,这里设置STATELESS(无状态)是在请求是不生成session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //配置权限
                .authorizeRequests()
                //对于登录login  验证码captchaImage  允许匿名访问
                .antMatchers("/login").anonymous()
                .antMatchers("/user/**").anonymous()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/report/**"/*,"/user/**"*/)  //需要对外暴露的资源路径
//                .access("hasRole('ADD')")
                .hasAnyAuthority("ROLE_ADD", "ROLE_UPDATE", "ROLE_DELETE")    //user角色和 admin角色都可以访问
                .antMatchers("/hello/**")
//                .access("hasRole('QUERY')")
                .hasAnyAuthority("ROLE_QUERY")    //user角色和 admin角色都可以访问
//                .hasAnyRole("ADMIN")    //admin角色可以访问
                //  除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated().and()//authenticated()要求在执 行该请求时，必须已经登录了应用
                .csrf().disable();

//        super.configure(http);
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
