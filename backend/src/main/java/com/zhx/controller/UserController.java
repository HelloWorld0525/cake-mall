package com.zhx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhx.common.Result;
import com.zhx.entity.User;
import com.zhx.service.UserService;
import com.zhx.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils; // 注意引入这个工具类
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ... 之前的 register 和 login 方法保持不变 ...

    // --- 以下是新增的管理接口 ---

    /**
     * 分页查询用户列表
     * GET /user/list?pageNum=1&pageSize=10&username=张三
     */
    @GetMapping("/list")
    public Result<IPage<User>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String username) {
        // 构造分页对象
        Page<User> page = new Page<>(pageNum, pageSize);

        // 构造查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like("username", username); // 模糊查询
        }
        wrapper.orderByDesc("id"); // 按ID倒序排列

        IPage<User> res = userService.page(page, wrapper);
        return Result.success(res);
    }

    /**
     * 删除用户
     * DELETE /user/10
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        boolean success = userService.removeById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("500", "删除失败");
        }
    }

    /**
     * 更新用户 (用于管理员修改用户信息的接口)
     * PUT /user
     */
    @PutMapping
    public Result<?> update(@RequestBody User user) {
        boolean success = userService.updateById(user);
        if (success) {
            return Result.success();
        } else {
            return Result.error("500", "更新失败");
        }
    }

    // ... 为了保持文件完整性，请确保 register 和 login 方法还在下面 ...
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        try {
            userService.register(user);
            return Result.success();
        } catch (Exception e) {
            return Result.error("500", "注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        try {
            User loginUser = userService.login(user);
            String token = JwtUtils.generateToken(loginUser.getId());
            Map<String, Object> data = new HashMap<>();
            data.put("user", loginUser);
            data.put("token", token);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("401", "登录失败: " + e.getMessage());
        }
    }
}