package com.zhx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取当前项目根目录下的 files 目录路径
        String path = System.getProperty("user.dir") + "/files/";

        // 确保目录存在
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        // 映射 URL: /files/** -> 本地 files 目录
        // 注意：file: 后面必须有三个斜杠或者正确处理路径，这里使用 addResourceLocations 的标准写法
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + path);
    }
}