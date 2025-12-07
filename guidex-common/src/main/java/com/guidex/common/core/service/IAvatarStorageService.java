package com.guidex.common.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author kled2
 * @date 2025/4/26
 */
public interface IAvatarStorageService {
    String save(MultipartFile file, Long userId) throws IOException;
    void delete(String avatarUrl);
    String saveImage(File imageFile);
}
