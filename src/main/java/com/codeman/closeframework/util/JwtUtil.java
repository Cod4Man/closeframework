package com.codeman.closeframework.util;

import com.codeman.closeframework.entity.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 23:16
 * @version: 1.0
 */
@Slf4j
public class JwtUtil {

    private static volatile Key KEY = null;

    private static String secret = "asd989234023hkjklfsdjf%^^$&*(!@#$#kjhjkadaksdj";

    private static Long expiration = 60 * 60 * 1000L;

    private static String header = "token";

    /**
     * 验证Token的合法性
     * @param token
     * @return
     */
    public static boolean verifyToken(String token, UserDetails userDetails) {
        String username = parseUserName(token);
        return (username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token));
    }

    private static boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析Token，加工出username
     * @param token
     * @return
     */
    public static String parseUserName(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.get("sub",String.class);
            log.debug("从令牌中获取用户名:" + username);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从令牌中获取数据声明,如果看不懂就看谁调用它
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token).getBody();
//            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public static String geneToken(String username, String password) throws Exception {
        UserDetailsService bean = SpringContextHolder.getBean("userDetailsService");
//        AuthenticationManager authenticationManager = bean.authenticationManagerBean();
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        UserDetail userDetail = (UserDetail) authenticate.getPrincipal();
        UserDetail userDetail = (UserDetail) bean.loadUserByUsername(username);
        log.debug("[JwtTokenUtils] generateToken "+userDetail.toString());
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userDetail.getUsername());
        claims.put("created", new Date());

        return generateToken(claims);

    }

    /**
     * 从claims生成令牌,如果看不懂就看谁调用它
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder().setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();
    }

    private static Key getKeyInstance() {
        if (KEY == null) {
            synchronized (JwtUtil.class) {
                if (KEY == null) {// 双重锁
                    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
                    KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
                }
            }
        }
        return KEY;
    }
}
