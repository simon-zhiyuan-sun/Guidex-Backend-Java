package com.guidex.common.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author kled2
 * @date 2025/4/27
 */
public interface IVideoStorageService {
    String saveVideo(MultipartFile file, Long userId) throws IOException;
    String toUrl(String objectName);
}
