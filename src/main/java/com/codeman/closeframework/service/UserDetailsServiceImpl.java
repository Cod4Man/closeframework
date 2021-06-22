package com.codeman.closeframework.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeman.closeframework.entity.User;
import com.codeman.closeframework.entity.UserDetail;
import com.codeman.closeframework.mapper.UserMapper;
import com.codeman.closeframework.util.MyMD5Util;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 22:35
 * @version: 1.0
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    Map<String, List<GrantedAuthority>> authMap = new HashMap<>();

    public UserDetailsServiceImpl() {
        // 可以改成查DB
        SimpleGrantedAuthority add = new SimpleGrantedAuthority("ROLE_ADD");
        SimpleGrantedAuthority update = new SimpleGrantedAuthority("ROLE_UPDATE");
        SimpleGrantedAuthority delete = new SimpleGrantedAuthority("ROLE_DELETE");
        SimpleGrantedAuthority query = new SimpleGrantedAuthority("ROLE_QUERY");
        authMap.put("admin", Lists.newArrayList(add, update, delete, query));
        authMap.put("selectuser", Lists.newArrayList(query));
        authMap.put("modifyuser", Lists.newArrayList(add, update, delete));
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("找不到该客户，请检查输入！");
        }
        // 权限
        List<GrantedAuthority> authList = authMap.get(username);
        // 可以用MD5重写
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return new UserDetail(username, MyMD5Util.encrypt(user.getPassword()), authList);
    }
}
