package com.repsy.restapi.config;

import com.repsy.storage.FileSystemStorageService;
import com.repsy.storage.ObjectStorage;
import com.repsy.storage.StorageService;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageConfig {

    @Bean
    public ObjectStorage objectStorage(MinioClient minioClient,
                                       @Value("${minio.bucket-name}") String bucketName) {
        return new ObjectStorage(minioClient, bucketName);
    }

    @Bean
    public FileSystemStorageService fileSystemStorageService(@Value("${storage.file.base-path}") String basePath) {

        return new FileSystemStorageService(Paths.get(basePath));
    }
}
