package com.codeman.closeframework.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author: zhanghongjie
 * @description:
 * @date: 2021/6/21 22:38
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("user")
@Accessors(chain = true)
public class User {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField
    private String username;
    @TableField
    private String password;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
