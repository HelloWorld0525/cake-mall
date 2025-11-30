package com.zhx.controller;

import com.zhx.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${server.port}")
    private String port;

    /**
     * 文件上传接口
     * POST /file/upload
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        // 1. 获取原文件名
        String originalFilename = file.getOriginalFilename();

        // 2. 获取后缀 (png, jpg)，防止空指针
        String suffix = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        // 3. 生成唯一文件名 (UUID)
        String newFileName = UUID.randomUUID().toString() + suffix;

        // 4. 定义存储路径 (项目根目录/files/)
        String basePath = System.getProperty("user.dir") + "/files/";
        File saveFile = new File(basePath + newFileName);

        // 确保父目录存在 (原生Java写法，替代Hutool)
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        // 5. 保存文件
        file.transferTo(saveFile);

        // 6. 返回访问 URL (http://localhost:8080/files/xxxx.jpg)
        String url = "http://localhost:" + port + "/files/" + newFileName;
        return Result.success(url);
    }
}