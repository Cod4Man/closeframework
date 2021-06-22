package com.codeman.closeframework.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 14:47
 * @version: 1.0
 */
@Component("executorServiceUtil")
public class ExecutorServiceUtil {
    private final static Integer CORE_THREAD_NUM;
    private final static Integer MAX_THREAD_NUM;
    private final static Integer KEEPALIVE_TIME;
    private final static TimeUnit KEEPALIVE_TIMEUNIT;
    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        int cpuCount = Runtime.getRuntime().availableProcessors();
        CORE_THREAD_NUM = cpuCount;
        MAX_THREAD_NUM = cpuCount;
        KEEPALIVE_TIME = 60;
        KEEPALIVE_TIMEUNIT = TimeUnit.SECONDS;
        ThreadPoolExecutor threadPoolExecutor
                = new ThreadPoolExecutor(CORE_THREAD_NUM, MAX_THREAD_NUM, KEEPALIVE_TIME, KEEPALIVE_TIMEUNIT, new LinkedBlockingQueue<>(15));
    }

    public ExecutorServiceUtil() {

    }

    public ExecutorService getExecutorService() {
        return threadPoolExecutor;
    }
}
