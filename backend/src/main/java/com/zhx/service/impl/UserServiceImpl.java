package com.zhx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhx.entity.User;
import com.zhx.mapper.UserMapper;
import com.zhx.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public boolean register(User user) {
        // 1. 检查用户名是否已存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        if (count(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 设置默认值 (非管理员，有效账户)
        user.setIsadmin(false);
        user.setIsvalidate(true);

        // 3. 保存用户 (TODO: 实际项目中密码应加密，例如使用 BCrypt)
        return save(user);
    }

    @Override
    public User login(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        wrapper.eq("password", user.getPassword()); // TODO: 实际应使用加密比对

        User dbUser = getOne(wrapper);
        if (dbUser == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (!dbUser.getIsvalidate()) {
            throw new RuntimeException("账户已被冻结");
        }

        // 注意：返回给前端时，不要把密码带回去，这里简单置空
        dbUser.setPassword(null);
        return dbUser;
    }
}