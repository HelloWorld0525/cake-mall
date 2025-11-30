package com.zhx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("goods")
public class Goods {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String cover;
    private String image1;
    private String image2;
    private Float price;
    private String intro;
    private Integer stock;
    private Integer typeId; // 对应数据库 type_id，MyBatis-Plus自动开启驼峰映射
}