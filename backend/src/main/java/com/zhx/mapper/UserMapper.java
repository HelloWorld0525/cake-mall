package com.zhx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhx.entity.User;

public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus 已内置常用 CRUD 方法，这里只需定义复杂自定义 SQL
}