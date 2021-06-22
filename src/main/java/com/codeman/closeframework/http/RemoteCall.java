package com.codeman.closeframework.http;

import com.alibaba.fastjson.JSON;
import com.codeman.closeframework.util.ZookeeperUtil;
import com.codeman.closeframework.vo.CommonInVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 15:47
 * @version: 1.0
 */
@Component("remoteCall")
@DependsOn("zookeeperUtil")
@Slf4j
public class RemoteCall {


    private static ZookeeperUtil zookeeperUtil;

    @Autowired
    public void setZookeeperUtil(ZookeeperUtil zookeeperUtil) {
        RemoteCall.zookeeperUtil = zookeeperUtil;
    }

    /**
     * 远程调用
     * @param jiaoyi 交易号
     * @param requestJson 请求json串
     * @return 响应json串
     */
    public static String call(String jiaoyi, String requestJson) throws IOException {
        String responseJson = "";
        String hostRandom = zookeeperUtil.getHostRandom(jiaoyi);
        String url = buildUrl(hostRandom);
        HttpClient httpClient = HttpClientBuilder
                .create()
                .setConnectionTimeToLive(1, TimeUnit.MINUTES) // 连接时间
                .build();
        HttpPost httpPost = new HttpPost(url);
        log.debug("远程调用开始【" + jiaoyi + "】,URL===" + url + ", requestJson=" + requestJson);
        CommonInVO commonInVO = JSON.parseObject(requestJson, CommonInVO.class);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("jiaoyi", jiaoyi));
        nvps.add(new BasicNameValuePair("username", commonInVO.getUsername()));
        nvps.add(new BasicNameValuePair("data", commonInVO.getData()));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        httpPost.setHeader("token" , "");
        RequestConfig requestConfig =  RequestConfig.custom()
                .setSocketTimeout(10000)  // 响应超时时间
                .setConnectTimeout(10000) // 连接超时时间
                .build();
        httpPost.setConfig(requestConfig);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        responseJson = EntityUtils.toString(entity);
        log.debug("远程调用结束【" + jiaoyi + "】,URL===" + url + ", responseJson=" + responseJson);
        return responseJson;
    }

    private static String buildUrl(String hostRandom) {
        String url = "";
        url = "http://" + hostRandom + "/report";
        return url;
    }
}
