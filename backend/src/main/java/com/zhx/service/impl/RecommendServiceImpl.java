package com.zhx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhx.entity.Recommend;
import com.zhx.mapper.RecommendMapper;
import com.zhx.service.RecommendService;
import org.springframework.stereotype.Service;

@Service
public class RecommendServiceImpl extends ServiceImpl<RecommendMapper, Recommend> implements RecommendService {
}