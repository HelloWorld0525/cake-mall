package com.zhx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhx.common.Result;
import com.zhx.dto.OrderDTO;
import com.zhx.entity.Goods;
import com.zhx.entity.Order;
import com.zhx.entity.OrderItem;
import com.zhx.service.GoodsService;
import com.zhx.service.OrderItemService;
import com.zhx.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询所有订单 (管理员用)
     */
    @GetMapping("/list")
    public Result<IPage<Order>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        page.addOrder(com.baomidou.mybatisplus.core.metadata.OrderItem.desc("datetime"));
        return Result.success(orderService.page(page));
    }

    /**
     * 前台查询我的订单 (用户用)
     */
    @GetMapping("/user/list")
    public Result<IPage<Order>> listByUser(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam Integer userId) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("datetime");
        return Result.success(orderService.page(page, wrapper));
    }

    /**
     * 提交订单
     */
    @PostMapping("/add")
    @Transactional
    public Result<?> addOrder(@RequestBody OrderDTO orderDto) {
        Order order = new Order();
        order.setTotal(orderDto.getTotal());
        order.setAmount(orderDto.getAmount());
        order.setPaytype(orderDto.getPaytype());
        order.setStatus(1);
        order.setName(orderDto.getName());
        order.setPhone(orderDto.getPhone());
        order.setAddress(orderDto.getAddress());
        order.setDatetime(LocalDateTime.now());
        order.setUserId(orderDto.getUserId());

        orderService.save(order);

        List<OrderItem> items = orderDto.getItems();
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
        }
        orderItemService.saveBatch(items);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Order order) {
        if (orderService.updateById(order)) {
            return Result.success();
        }
        return Result.error("500", "更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        QueryWrapper<OrderItem> itemWrapper = new QueryWrapper<>();
        itemWrapper.eq("order_id", id);
        orderItemService.remove(itemWrapper);

        if (orderService.removeById(id)) {
            return Result.success();
        }
        return Result.error("500", "删除失败");
    }

    @GetMapping("/items/{orderId}")
    public Result<List<Map<String, Object>>> getOrderItems(@PathVariable Integer orderId) {
        QueryWrapper<OrderItem> query = new QueryWrapper<>();
        query.eq("order_id", orderId);
        List<OrderItem> itemList = orderItemService.list(query);

        List<Map<String, Object>> result = new ArrayList<>();
        for (OrderItem item : itemList) {
            Map<String, Object> map = new HashMap<>();
            map.put("price", item.getPrice());
            map.put("amount", item.getAmount());

            Goods goods = goodsService.getById(item.getGoodsId());
            if (goods != null) {
                map.put("goodsName", goods.getName());
                map.put("goodsCover", goods.getCover());
            } else {
                map.put("goodsName", "已下架或删除");
                map.put("goodsCover", "");
            }
            result.add(map);
        }
        return Result.success(result);
    }
}