package com.zhx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 扫描 Mapper 接口所在的包，自动生成实现类
@MapperScan("com.zhx.mapper")
public class CakeMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(CakeMallApplication.class, args);
        System.out.println("====== 蛋糕商城后端服务启动成功 ======");
    }
}