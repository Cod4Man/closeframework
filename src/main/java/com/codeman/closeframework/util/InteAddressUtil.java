package com.codeman.closeframework.util;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 15:21
 * @version: 1.0
 */
@Slf4j
public class InteAddressUtil {

    public static InetAddress getInetAddress() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ipHost = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ipHost = (InetAddress) addresses.nextElement();
                if (ipHost != null && ipHost instanceof Inet4Address) {
                    log.info("本机的HOSTIP = " + ipHost.getHostAddress());
                    log.info("本机的HOSTNAME = " + ipHost.getHostName());
                    return ipHost;
                }
            }
        }
        return ipHost;
    }

    /*public static void main(String[] args) throws SocketException {
        getInetAddress();
    }*/
}
