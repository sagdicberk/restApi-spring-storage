package com.repsy.restapi.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() throws Exception {
        // MinioClient oluşturuluyor
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();

        // Bucket'ın var olup olmadığını kontrol et
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        // Eğer bucket yoksa, yeni bir bucket oluştur
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            System.out.println("Bucket oluşturuldu: " + bucketName);
        } else {
            System.out.println("Bucket zaten var: " + bucketName);
        }

        return minioClient;
    }
}
