package com.zhx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("recommend")
public class Recommend {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer type; // 1:热销, 2:新品, 3:条幅推荐
    private Integer goodsId;
}