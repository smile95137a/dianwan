package com.one.onekuji.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        staticPicturePath = picturePath.endsWith("/") ? picturePath : picturePath + "/";
        staticPicturePathMapping = picturePathMapping.endsWith("/") ? picturePathMapping : picturePathMapping + "/";
    }

    public static String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileName = file.getOriginalFilename();  // 获取原始文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 获取文件后缀名
        String filePath = staticPicturePath + fileName;
        File dest = new File(filePath);


        try {
            Files.createDirectories(Paths.get(staticPicturePath));

            // 将文件保存到目标路径
            file.transferTo(dest);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败", e);
        }

        String finalFileName = staticPicturePathMapping + fileName;
        System.out.println("File uploaded to: " + finalFileName);
        return finalFileName;
    }
}
