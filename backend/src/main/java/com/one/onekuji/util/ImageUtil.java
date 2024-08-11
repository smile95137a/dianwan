package com.one.onekuji.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageUtil {

    @Value("${pictureFile.path}")
    private String picturePath;

    @Value("${pictureFile.path-mapping}")
    private String picturePathMapping;

    private static String staticPicturePath;
    private static String staticPicturePathMapping;

    @PostConstruct
    public void init() {
        // 初始化时，确保路径以斜杠结尾
        staticPicturePath = picturePath.endsWith("/") ? picturePath : picturePath + "/";
        staticPicturePathMapping = picturePathMapping.endsWith("/") ? picturePathMapping : picturePathMapping + "/";
    }

    public static String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileName = file.getOriginalFilename();  // 获取原始文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 获取文件后缀名

        // 生成新的文件名
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(staticPicturePath + fileName);

        try {
            // 确保目录存在
            Files.createDirectories(Paths.get(staticPicturePath));

            // 将文件传输到指定的目标文件
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败", e);
        }

        // 生成并返回文件的访问 URL
        String finalFileName = "https://6ce2-2402-7500-4dc-948-7df7-96b-239b-ae80.ngrok-free.app" + staticPicturePathMapping + fileName;
        System.out.println("File uploaded to: " + finalFileName);
        return finalFileName;
    }
}
