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

        // 通用上传方法
        public static String upload(MultipartFile file) {
            return uploadFile(file, false);
        }

        // CKEditor 专用上传方法
        public static String uploadForCKEditor(MultipartFile file) {
            return uploadFile(file, true);  // 不裁剪或调整尺寸
        }

        // 统一处理上传的方法
        private static String uploadFile(MultipartFile file, boolean isForCKEditor) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }

            // 生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            String filePath = staticPicturePath + uniqueFileName;
            File dest = new File(filePath);

            try {
                Files.createDirectories(Paths.get(staticPicturePath));

                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();

                if (isForCKEditor) {
                    // CKEditor 的图片上传逻辑，不调整尺寸
                    Thumbnails.of(originalImage)
                            .scale(1)
                            .toFile(dest);
                } else {
                    // 普通图片上传逻辑，调整为 400x400
                    if (width != height) {
                        // 如果不是正方形，先裁剪为正方形
                        int size = Math.min(width, height);
                        Thumbnails.of(originalImage)
                                .sourceRegion(Positions.CENTER, size, size)
                                .size(400, 400)
                                .outputQuality(0.85)
                                .toFile(dest);
                    } else {
                        // 如果已经是正方形，直接调整大小
                        Thumbnails.of(originalImage)
                                .size(400, 400)
                                .outputQuality(0.85)
                                .toFile(dest);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("File upload failed", e);
            }

            // 返回可访问的 URL 路径
            return staticPicturePathMapping + uniqueFileName;
        }
    }

