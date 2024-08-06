package com.one.onekuji.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImageUtil {

    @Value("${pictureFile.path}")
    private static String picturePath;

    @Value("${pictureFile.path-mapping}")
    private  static String picturePath_mapping;

    public static String upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(picturePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String final_fileName = "http://localhost:8282" + picturePath_mapping + fileName;
        System.out.println(final_fileName);
        return final_fileName;
    }
}
