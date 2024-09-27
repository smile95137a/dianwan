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
        staticPicturePath = picturePath.endsWith("/") ? picturePath : picturePath + "/";
        staticPicturePathMapping = picturePathMapping.endsWith("/") ? picturePathMapping : picturePathMapping + "/";
    }

    public static String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Generate a unique file name
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        String filePath = staticPicturePath + uniqueFileName;
        File dest = new File(filePath);

        try {
            Files.createDirectories(Paths.get(staticPicturePath));

            // Save the file to the target path
            file.transferTo(dest);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed", e);
        }

        // Return the URL mapping path
        return staticPicturePathMapping + uniqueFileName;
    }

}
