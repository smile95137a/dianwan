package com.one.onekuji.util;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

            // Convert MultipartFile to BufferedImage
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // Check the image dimensions
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // If the image is larger than 400x400, resize and crop it
            if (width > 400 || height > 400) {
                Thumbnails.of(originalImage)
                        .size(400, 400)  // Set maximum size to 400x400
                        .crop(Positions.CENTER)  // Crop from center if necessary
                        .toFile(dest);  // Save the modified image
            } else {
                // Save the original image if no resizing is needed
                file.transferTo(dest);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed", e);
        }

        // Return the URL mapping path
        return staticPicturePathMapping + uniqueFileName;
    }

}
