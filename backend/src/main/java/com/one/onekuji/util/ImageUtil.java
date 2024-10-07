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

                if (isForCKEditor) {
                    // CKEditor 的图片上传逻辑，不需要修改尺寸
                    file.transferTo(dest);  // 直接保存原图
                } else {
                    // 普通图片上传逻辑，进行尺寸处理
                    BufferedImage originalImage = ImageIO.read(file.getInputStream());

                    int width = originalImage.getWidth();
                    int height = originalImage.getHeight();

                    // 图片超过 400x400 尺寸时，进行裁剪和压缩
                    if (width > 400 || height > 400) {
                        // 确保裁剪到 400x400 的正方形
                        Thumbnails.of(originalImage)
                                .sourceRegion(Positions.CENTER, 400, 400)  // 从中心裁剪出 400x400
                                .size(400, 400)  // 强制大小为 400x400
                                .keepAspectRatio(false)  // 禁止保持原始比例，强制为正方形
                                .toFile(dest);
                    } else {
                        // 保持原始图片尺寸
                        file.transferTo(dest);
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

