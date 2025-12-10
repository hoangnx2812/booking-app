package com.example.postservice.service;

public interface MinioService {
    void uploadBase64Image(String base64, String objectName, String contentType);
    void deleteFile(String objectName);
    String getPresignedUrl(String objectName);
}
