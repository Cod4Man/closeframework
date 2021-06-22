package com.codeman.closeframework.controller;

import com.alibaba.fastjson.JSON;
import com.codeman.closeframework.http.RemoteCall;
import com.codeman.closeframework.vo.CommonInVO;
import com.codeman.closeframework.vo.CommonOutVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 16:04
 * @version: 1.0
 */
@Component("A000D002Service")
@Slf4j
public class A000D002Service {


    public String a000D002(HttpServletRequest req) {
        String data = req.getParameter("data");
        String jiaoyi = req.getParameter("jiaoyi");
        String toJiaoyi = req.getParameter("toJiaoyi");
        String username = req.getParameter("username");
        if (!StringUtils.isEmpty(toJiaoyi)) {
            // 远程调用
            CommonInVO cc = new CommonInVO();
            try {
                cc.setUsername(username);
                cc.setData("你好，这里是A000D002Service");
                String resp = RemoteCall.call(toJiaoyi, JSON.toJSONString(cc));
                log.info("respppp.ToJiaoYI 【" + toJiaoyi + "】= " + resp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CommonOutVO commonOutVO = new CommonOutVO();
        commonOutVO.setCode(200);
        commonOutVO.setData("hi, 【" + username + "】 . this is A000D002.a000D002, data=" + data);
        return JSON.toJSONString(commonOutVO);
    }
}
