package com.guidex.common.core.service.impl;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.guidex.common.core.service.IAvatarStorageService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author kled2
 * @date 2025/4/26
 */
@Service
public class GcsAvatarStorageService implements IAvatarStorageService {
    private final Storage storage;
    private final String bucketName;
    public static final Logger log = LoggerFactory.getLogger(GcsAvatarStorageService.class);


    public GcsAvatarStorageService(
            @Value("${gcp.storage.credentials-file}") String credPath,
            @Value("${gcp.storage.bucket}") String bucketName
    ) throws IOException {
        // 加载 JSON Key
        ServiceAccountCredentials creds = ServiceAccountCredentials.fromStream(
                new FileInputStream(credPath)
        );
        this.storage = StorageOptions.newBuilder()
                .setCredentials(creds)
                .build()
                .getService();
        this.bucketName = bucketName;
    }

    @Override
    public String save(MultipartFile file, Long userId) throws IOException {
        long maxAvatarSize = 2L * 1024 * 1024; // 2MB
        if (file.getSize() > maxAvatarSize) {
            throw new IllegalArgumentException("头像不能超过 2MB");
        }
        // 校验类型
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            throw new IllegalArgumentException("仅支持图片文件");
        }
        // 生成唯一名
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String shortId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String objectName = String.format("avatars/%d_%s.%s", userId, shortId, ext);

        // 上传
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(ct).build();
        storage.create(blobInfo, file.getBytes());

        // 返回公开 URL（前面已设置过 public-read）
        return String.format("https://storage.googleapis.com/%s/%s",
                bucketName, objectName);
    }

    @Override
    public void delete(String avatarUrl) {
        if (StringUtils.isEmpty(avatarUrl) || !avatarUrl.contains(bucketName)) {
            return;
        }
        String obj = avatarUrl.substring(
                avatarUrl.indexOf(bucketName) + bucketName.length() + 1);
        storage.delete(BlobId.of(bucketName, obj));
    }

    @Override
    public String saveImage(File imageFile) {
        try {
            if (imageFile == null || !imageFile.exists()) {
                log.error("封面图上传失败：文件为空或不存在，file={}", imageFile);
                return null;
            }

            // ✅ 超过 200KB 则压缩
            // final long maxSizeBytes = 200 * 1024;
            // if (imageFile.length() > maxSizeBytes) {
            //     log.info("原始封面图超出200KB，开始压缩...");
            //     imageFile = compressImageToTargetSize(imageFile, maxSizeBytes);
            // }

            String ext = FilenameUtils.getExtension(imageFile.getName());
            if (ext == null || ext.isEmpty()) {
                ext = "jpg";
            }

            String shortId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            String objectName = String.format("covers/%s.%s", shortId, ext);

            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("image/" + ext)
                    .build();

            try (FileInputStream fis = new FileInputStream(imageFile)) {
                storage.create(blobInfo, fis);
            }

            return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);

        } catch (Exception e) {
            log.error("封面图上传失败: {}", e.getMessage(), e);
            return null;
        }
    }

    private File compressImageToTargetSize(File originalFile, long maxSizeBytes) throws IOException {
        BufferedImage image = ImageIO.read(originalFile);
        File compressedFile = new File(originalFile.getParent(), "compressed_" + originalFile.getName());

        float quality = 0.9f;
        for (int i = 0; i < 10; i++) {
            try (FileOutputStream fos = new FileOutputStream(compressedFile)) {
                Thumbnails.of(image)
                        .scale(1.0) // 不缩放，只压缩
                        .outputQuality(quality)
                        .outputFormat("jpg")
                        .toOutputStream(fos);
            }

            if (compressedFile.length() <= maxSizeBytes) {
                log.info("图片压缩成功，最终大小={}KB", compressedFile.length() / 1024);
                return compressedFile;
            }

            quality -= 0.1f;
            if (quality <= 0.1f) break;
        }

        log.warn("压缩失败，无法将图片压缩至 {}KB 以下，使用最后版本上传", maxSizeBytes / 1024);
        return compressedFile.exists() ? compressedFile : originalFile;
    }



}

