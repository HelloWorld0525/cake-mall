package com.zhx.common;

import lombok.Data;

/**
 * 统一响应结果封装类
 * 前后端分离项目中，后端返回给前端的数据格式需保持一致
 */
@Data
public class Result<T> {

    // 状态码：200成功，500失败，401未登录等
    private String code;

    // 提示信息
    private String msg;

    // 返回的数据
    private T data;

    // 成功时的静态构造方法
    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 失败时的静态构造方法
    public static <T> Result<T> error(String code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}