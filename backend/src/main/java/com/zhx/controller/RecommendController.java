package com.zhx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhx.common.Result;
import com.zhx.entity.Goods;
import com.zhx.entity.Recommend;
import com.zhx.service.GoodsService;
import com.zhx.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 前台展示接口：直接返回商品列表
     * type: 1热销, 2新品, 3轮播
     */
    @GetMapping("/goods")
    public Result<List<Goods>> getGoodsByType(@RequestParam Integer type) {
        QueryWrapper<Recommend> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.orderByDesc("id");
        List<Recommend> list = recommendService.list(wrapper);

        List<Goods> goodsList = new ArrayList<>();
        for (Recommend r : list) {
            Goods goods = goodsService.getById(r.getGoodsId());
            if (goods != null) {
                goodsList.add(goods);
            }
        }
        return Result.success(goodsList);
    }

    /**
     * 后台管理接口：分页查询推荐列表（包含 recommendId 用于删除）
     */
    @GetMapping("/page")
    public Result<IPage<Map<String, Object>>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam Integer type) {
        Page<Recommend> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Recommend> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.orderByDesc("id");

        IPage<Recommend> recommendPage = recommendService.page(page, wrapper);

        // 转换结果，封装商品信息
        Page<Map<String, Object>> resultPage = new Page<>();
        resultPage.setCurrent(recommendPage.getCurrent());
        resultPage.setSize(recommendPage.getSize());
        resultPage.setTotal(recommendPage.getTotal());

        List<Map<String, Object>> list = new ArrayList<>();
        for (Recommend r : recommendPage.getRecords()) {
            Goods goods = goodsService.getById(r.getGoodsId());
            if (goods != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", r.getId()); // 推荐表的主键
                map.put("goodsId", goods.getId());
                map.put("name", goods.getName());
                map.put("cover", goods.getCover());
                map.put("price", goods.getPrice());
                list.add(map);
            }
        }
        resultPage.setRecords(list);

        return Result.success(resultPage);
    }

    /**
     * 添加推荐
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody Recommend recommend) {
        // 简单查重：防止重复添加同一个商品到同一个推荐位
        QueryWrapper<Recommend> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", recommend.getGoodsId());
        wrapper.eq("type", recommend.getType());
        if (recommendService.count(wrapper) > 0) {
            return Result.error("500", "该商品已在推荐列表中");
        }

        if (recommendService.save(recommend)) {
            return Result.success();
        }
        return Result.error("500", "添加失败");
    }

    /**
     * 移除推荐
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        if (recommendService.removeById(id)) {
            return Result.success();
        }
        return Result.error("500", "移除失败");
    }
}