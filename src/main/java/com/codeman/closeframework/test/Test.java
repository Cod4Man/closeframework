package com.codeman.closeframework.test;

import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 18:31
 * @version: 1.0
 */
public class Test {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, InterruptedException {
//        Class<?> aClass = Class.forName("com.codeman.closeframework.controller.A000D001Service");
////        Method method = aClass.getMethod();
//        for (Method method : aClass.getMethods()) {
////            System.out.println(method.getName());
//        }
//        Method a000D001 = aClass.getMethod("a000D001", String.class);
//        System.out.println(a000D001.invoke(aClass.newInstance(), "你好"));

//        CommonInVO commonInVO = new CommonInVO();
//        commonInVO.setUsername("zhangsan");
//        commonInVO.setData("数据");
//        commonInVO.setJiaoyi("A000D002");
//        System.out.println(JSON.toJSON(commonInVO));

//        testAtomicIntegerWithLock();
        testAtomicIntegerWithCAS();
//        test02();
    }

    private static void test02() {
        Map<Integer, Integer> map = new ConcurrentHashMap<>();

        new Thread(() -> {
            map.put(1,1);
        }).start();
        System.out.println(map);
    }

    private static void testAtomicIntegerWithCAS() throws InterruptedException {
        AtomicInteger currNum = new AtomicInteger(0);
        int maxSize = 5;
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        map.put(0,0);
        map.put(1,0);
        map.put(2,0);
        map.put(3,0);
        map.put(4,0);
        CountDownLatch countDownLatch  = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                /*map.putIfAbsent(currNum.updateAndGet(prev -> {
                    return (currNum.get() + 1) % maxSize;
                }), 0);*/
                map.computeIfPresent(currNum.updateAndGet(prev -> {
                    return (currNum.get() + 1) % maxSize;
                }), (k, ov) -> ov+1);

                countDownLatch.countDown();
            },  "线程" + i).start();
        }
        countDownLatch.await();
        System.out.println(map);

    }

    private static void testAtomicIntegerWithLock() {
        AtomicInteger currNum = new AtomicInteger(0);
        int maxSize = 5;
        ReentrantLock lock = new ReentrantLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    lock.lock();
                    int i1 = currNum.get();
                    currNum.compareAndSet(i1, ++i1 % maxSize);
                    System.out.println(Thread.currentThread().getName() + "  " + currNum.get());
                } finally {
                    lock.unlock();

                }
            },  "线程" + i).start();
        }
    }
}
