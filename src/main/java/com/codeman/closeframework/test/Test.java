package com.codeman.closeframework.test;

import com.alibaba.fastjson.JSON;
import com.codeman.closeframework.vo.CommonInVO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 18:31
 * @version: 1.0
 */
public class Test {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
//        Class<?> aClass = Class.forName("com.codeman.closeframework.controller.A000D001Service");
////        Method method = aClass.getMethod();
//        for (Method method : aClass.getMethods()) {
////            System.out.println(method.getName());
//        }
//        Method a000D001 = aClass.getMethod("a000D001", String.class);
//        System.out.println(a000D001.invoke(aClass.newInstance(), "你好"));

        CommonInVO commonInVO = new CommonInVO();
        commonInVO.setUsername("zhangsan");
        commonInVO.setData("数据");
        commonInVO.setJiaoyi("A000D002");
        System.out.println(JSON.toJSON(commonInVO));
    }
}
