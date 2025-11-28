package com.example.postservice.service.impl;

import com.example.postservice.service.MinioService;
import io.minio.MinioClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioServiceImpl implements MinioService {
    MinioClient minioClient;

    @NonFinal
    @Value("${minio.url}")
    String minioUrl;

    @NonFinal
    @Value("${minio.attachments-bucket}")
    String attachmentsBucket;

}
