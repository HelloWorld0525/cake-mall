package com.zhx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 用户实体类
 * 对应数据库表: user
 * </p>
 *
 * @author zhx
 */
@Data
@TableName("user")
public class User {

    /**
     * 用户表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户电话
     */
    private String phone;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 是否为管理员 (0:否, 1:是)
     * 在 Java 中 boolean 对应 MySQL 的 BIT(1)
     * MyBatis-Plus 会自动处理 boolean/BIT 转换
     */
    private Boolean isadmin;

    /**
     * 账户是否有效 (0:无效, 1:有效)
     */
    private Boolean isvalidate;
}