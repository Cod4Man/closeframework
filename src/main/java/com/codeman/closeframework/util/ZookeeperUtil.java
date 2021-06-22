package com.codeman.closeframework.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.codeman.closeframework.servlet.InitServlet;
import com.codeman.closeframework.util.balance.LoadBalancer;
import com.codeman.closeframework.util.balance.RotateLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 14:59
 * @version: 1.0
 */
@Component("zookeeperUtil")
public class ZookeeperUtil {
    private static final Logger log = LoggerFactory.getLogger(ZookeeperUtil.class);
    private volatile ZooKeeper zooKeeper = null;
    // ZK服务器地址
    // 集群可用'，'分隔"192.168.1.170:2181,192.168.1.171:2181"
    @Value("${zookeeperutil.connectString}")
    private String connectString;
    // 会话超时时间
    @Value("${zookeeperutil.sessionTimeout}")
    private int sessionTimeout;
    // 交易存放的根节点
    @Value("${zookeeperutil.parentPath}")
    private String parentPath;
    @Value("${port}")
    private String port;

    public ZookeeperUtil() {
        log.info("new zookeeperUtil（）");
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    private void connetZk() throws IOException {
        if (zooKeeper == null) {
            synchronized (this) {
                if (zooKeeper == null) {
                    zooKeeper = new ZooKeeper(connectString, sessionTimeout, (event) -> {
                        log.debug("zookeeper.watch.event" + event);
                    });
                }
            }
        }
    }

    public boolean createNode(String jiaoyi) {
        try {
            // 1. 连接zk
            connetZk();
            String host = InteAddressUtil.getInetAddress().getHostAddress() + ":" + port;
            log.debug("createNode.host+port=====" + host);
            // 2. 注册节点
            if (zooKeeper.exists(parentPath.substring(0, parentPath.length() - 1), false) == null) {
                String s = zooKeeper.create(parentPath.substring(0, parentPath.length() - 1), host.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.debug("交易【" + s + "】 注册成功！");
            }
            if (zooKeeper.exists(parentPath + jiaoyi, false) == null) {
                String s0 = zooKeeper.create(parentPath + jiaoyi, host.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.debug("交易【" + s0 + "】 注册成功！");
            }

            String s1 = zooKeeper.create(parentPath + jiaoyi + "/" + jiaoyi, host.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.debug("交易【" + s1 + "】 注册成功！");
            return true;
        } catch (IOException e) {
            log.error("createNode.IOException.Error==", e);
        } catch (InterruptedException e) {
            log.error("createNode.InterruptedException.Error==", e);
        } catch (KeeperException e) {
            log.error("createNode.KeeperException.Error==", e);
        }
        return false;
    }

    public String getHostRandom(String jiaoyi) {
        List<String> servers = new ArrayList<>();
        try {
            // 1. 连接zk
            connetZk();
            // 2. 通过交易号负载均衡获取其中一个host
            List<String> childrens = zooKeeper.getChildren(parentPath + jiaoyi, true);
            log.debug("交易【" + jiaoyi + "】底下有：" + childrens);
            // 遍历节点，获取所有带序号的子节点，子节点中存放的数据为host
            childrens.forEach(children -> {
                try {
                    byte[] data = zooKeeper.getData(parentPath + jiaoyi + "/" + children, false, null);
                    servers.add(new String(data));
                } catch (InterruptedException e) {
                    log.error("getHostRandom.InterruptedException.Error==", e);
                } catch (KeeperException e) {
                    log.error("getHostRandom.KeeperException.Error==", e);
                }
            });
        } catch (IOException e) {
            log.error("getHostRandom.IOException.Error==", e);
        } catch (InterruptedException e) {
            log.error("getHostRandom.InterruptedException.Error==", e);
        } catch (KeeperException e) {
            log.error("getHostRandom.KeeperException.Error==", e);
        }
        return myLoadBalance(servers);
    }

    /**
     * 轮询算法
     *
     * @param servers
     * @return
     */
    private String myLoadBalance(List<String> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        LoadBalancer loadBalancer = new RotateLoadBalancer(servers);
        return loadBalancer.get();
    }

}
