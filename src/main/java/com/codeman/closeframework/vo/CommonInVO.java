package com.codeman.closeframework.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 19:58
 * @version: 1.0
 */
@Data
public class CommonInVO implements Serializable {

    private String jiaoyi;
    private String username;
    private String data;
    private String toJiaoyi;

}
