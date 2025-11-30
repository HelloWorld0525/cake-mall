package com.zhx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhx.entity.Goods;
import com.zhx.mapper.GoodsMapper;
import com.zhx.service.GoodsService;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
}