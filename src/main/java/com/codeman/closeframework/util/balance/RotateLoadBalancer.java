package com.codeman.closeframework.util.balance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/22 19:36
 * @version: 1.0
 */
public class RotateLoadBalancer extends AbstractLoadBalancer {

    private AtomicInteger atomicInteger ;

    public RotateLoadBalancer() {
        atomicInteger = new AtomicInteger(0);
    }

    public RotateLoadBalancer(List<String> urls) {
        this();
        setUrls(urls);
    }



    @Override
    protected String balance(List<String> urls) {
        int i = 0;
        int next = 0;
        do {
            i = atomicInteger.get();
            next = (++i) % urls.size();
        } while (!atomicInteger.compareAndSet(i, next));

        return urls.get(next);
    }
}
