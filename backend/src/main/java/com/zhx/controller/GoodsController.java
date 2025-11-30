package com.zhx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhx.common.Result;
import com.zhx.entity.Goods;
import com.zhx.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping
    public Result<?> save(@RequestBody Goods goods) {
        return goodsService.save(goods) ? Result.success() : Result.error("500", "保存失败");
    }

    @PutMapping
    public Result<?> update(@RequestBody Goods goods) {
        return goodsService.updateById(goods) ? Result.success() : Result.error("500", "更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return goodsService.removeById(id) ? Result.success() : Result.error("500", "删除失败");
    }

    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable Integer id) {
        return Result.success(goodsService.getById(id));
    }

    /**
     * 列表查询 (支持分页、名称模糊搜索、类目筛选)
     * 这里的 list 方法支持 name 和 typeId 参数
     */
    @GetMapping("/list")
    public Result<IPage<Goods>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) Integer typeId) {
        Page<Goods> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(name)) {
            wrapper.like("name", name);
        }
        if (typeId != null) {
            wrapper.eq("type_id", typeId);
        }

        wrapper.orderByDesc("id");
        return Result.success(goodsService.page(page, wrapper));
    }
}