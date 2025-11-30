package com.zhx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("`order`") // 关键修改：加上反引号 `order`，避免与 MySQL 关键字冲突
public class Order {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Float total;
    private Integer amount;
    private Integer status; // 0未支付/1已支付
    private Integer paytype; // 1微信/2支付宝
    private String name;
    private String phone;
    private String address;
    private LocalDateTime datetime;
    private Integer userId;
}