package com.smartcity.service.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    public String uploadFile(MultipartFile file, String  directory);
}
