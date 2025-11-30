package com.zhx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhx.entity.User;

public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册成功的用户对象，如果失败抛出异常或返回null
     */
    boolean register(User user);

    /**
     * 用户登录
     * @param user 包含用户名和密码的对象
     * @return 登录成功后的 User 对象 (包含 Token)
     */
    User login(User user);
}