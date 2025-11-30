package com.zhx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhx.common.Result;
import com.zhx.entity.Type;
import com.zhx.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 保存类目
     */
    @PostMapping
    public Result<?> save(@RequestBody Type type) {
        if (typeService.save(type)) {
            return Result.success();
        }
        return Result.error("500", "保存失败");
    }

    /**
     * 更新类目
     */
    @PutMapping
    public Result<?> update(@RequestBody Type type) {
        if (typeService.updateById(type)) {
            return Result.success();
        }
        return Result.error("500", "更新失败");
    }

    /**
     * 删除类目
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        if (typeService.removeById(id)) {
            return Result.success();
        }
        return Result.error("500", "删除失败");
    }

    /**
     * 列表查询（分页，用于后台管理表格）
     */
    @GetMapping("/list")
    public Result<IPage<Type>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Type> page = new Page<>(pageNum, pageSize);
        // 按ID倒序
        page.addOrder(com.baomidou.mybatisplus.core.metadata.OrderItem.desc("id"));
        return Result.success(typeService.page(page));
    }

    /**
     * 查询所有（不分页，用于下拉框选择）
     */
    @GetMapping("/findAll")
    public Result<List<Type>> findAll() {
        return Result.success(typeService.list());
    }
}