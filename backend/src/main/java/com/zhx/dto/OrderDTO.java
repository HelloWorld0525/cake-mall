package com.zhx.dto;

import com.zhx.entity.OrderItem;
import lombok.Data;
import java.util.List;

/**
 * 用于接收前端提交的订单数据
 */
@Data
public class OrderDTO {
    // 订单总金额
    private Float total;
    // 商品总数量
    private Integer amount;
    // 支付方式 (1微信 2支付宝)
    private Integer paytype;
    // 收货人信息
    private String name;
    private String phone;
    private String address;
    // 下单用户ID
    private Integer userId;

    // 购买的商品列表
    private List<OrderItem> items;
}