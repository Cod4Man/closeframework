package com.codeman.closeframework.util.balance;

import java.util.List;
import java.util.Random;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/22 19:36
 * @version: 1.0
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {

    private final static Random RANDOM = new Random(System.currentTimeMillis());

    @Override
    protected String balance(List<String> urls) {
        return urls.get(RandomLoadBalancer.RANDOM.nextInt(urls.size()));
    }
}
