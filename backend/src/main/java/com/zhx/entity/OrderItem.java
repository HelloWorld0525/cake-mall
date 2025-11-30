package com.zhx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("orderitem")
public class OrderItem {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Float price;
    private Integer amount;
    private Integer goodsId;
    private Integer orderId;
}