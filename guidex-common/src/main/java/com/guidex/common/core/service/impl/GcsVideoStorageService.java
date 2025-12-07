package com.guidex.common.core.service.impl;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import com.guidex.common.core.service.IVideoStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * @author kled2
 * @date 2025/4/27
 */
@Service
@Slf4j
public class GcsVideoStorageService implements IVideoStorageService {

    @Value("${gcp.storage.bucket}")    private String bucketName;
    private final Storage storage;

    public GcsVideoStorageService(
            @Value("${gcp.storage.credentials-file}") String credPath
    ) throws IOException {
        ServiceAccountCredentials creds =
                ServiceAccountCredentials.fromStream(new FileInputStream(credPath));
        this.storage = StorageOptions.newBuilder()
                .setCredentials(creds)
                .build().getService();
    }

    @Override
    public String saveVideo(MultipartFile file, Long userId) throws IOException {
        String ct = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();

        log.info("[saveVideo] 接收到文件: 文件名={}, Content-Type={}, 大小={} bytes", originalFilename, ct, size);

        // 校验格式
        // if (ct == null || !"video/mp4".equals(ct)) {
        //     log.error("[saveVideo] 文件格式不支持: Content-Type={}", ct);
        //     throw new IllegalArgumentException("只支持MP4视频格式");
        // }

        // 校验大小
        if (size == 0) {
            log.error("[saveVideo] 上传文件为空");
            throw new IllegalArgumentException("上传文件不能为空");
        }
        long max = 100L * 1024 * 1024; // 100MB
        if (size > max) {
            log.error("[saveVideo] 文件过大: size={} bytes, max={} bytes", size, max);
            throw new IllegalArgumentException("视频不能超过 100MB");
        }

        // 生成 objectName
        String ext = FilenameUtils.getExtension(originalFilename);
        String shortId = UUID.randomUUID().toString().replace("-", "").substring(0,8);
        String objectName = String.format("videos/%d_%s.%s", userId, shortId, ext);
        log.info("[saveVideo] 生成 objectName: {}", objectName);

        // 上传至 GCS
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(ct).build();
        storage.create(blobInfo, file.getBytes());
        log.info("[saveVideo] 文件已上传至 GCS: {}", objectName);

        // 验证 GCS 上传结果
        Blob blob = storage.get(blobId);
        if (blob == null) {
            log.error("[saveVideo] GCS 中未找到文件: {}", objectName);
            throw new IOException("GCS 上传失败");
        } else {
            log.info("[saveVideo] GCS 文件大小: {} bytes", blob.getSize());
        }

        return objectName;
    }

    @Override
    public String toUrl(String objectName) {
        return String.format("https://storage.googleapis.com/%s/%s",
                bucketName, objectName);
    }

    // private long getVideoDurationSeconds(File file) throws IOException {
    //     ProcessBuilder builder = new ProcessBuilder(
    //             "ffprobe",
    //             "-v", "error",
    //             "-show_entries", "format=duration",
    //             "-of", "default=noprint_wrappers=1:nokey=1",
    //             file.getAbsolutePath()
    //     );
    //     Process process = builder.start();
    //
    //     try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
    //         String line = reader.readLine();
    //         return (long) Double.parseDouble(line);
    //     } catch (Exception e) {
    //         throw new IOException("视频时长解析失败", e);
    //     }
    // }
}
