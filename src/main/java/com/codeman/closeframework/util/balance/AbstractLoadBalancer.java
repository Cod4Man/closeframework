package com.codeman.closeframework.util.balance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/22 19:29
 * @version: 1.0
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    private List<String> urls = new ArrayList<>();

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String get() {
        if (urls == null) {
            urls = new ArrayList<>();
        }
        return balance(urls);
    }

    protected abstract String balance(List<String> urls);
}
