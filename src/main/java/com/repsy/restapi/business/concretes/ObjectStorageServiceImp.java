package com.repsy.restapi.business.concretes;

import com.repsy.restapi.business.abstracts.StorageService;
import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service("object-storage")
public class ObjectStorageServiceImp implements StorageService {
    private final MinioClient minioClient;
    private final String bucketName;


    public ObjectStorageServiceImp(MinioClient minioClient, @Value("${minio.bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public void save(String path, InputStream inputStream) throws IOException {
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(inputStream, inputStream.available(), -1) // stream, size, partSize
                    .build();

            // Asynchronous upload
            ObjectWriteResponse response = minioClient.putObject(args);

            // Optionally, handle the response (e.g., logging, etc.)
            System.out.println("File uploaded successfully. ETag: " + response.etag());

        } catch (MinioException | InvalidKeyException |
                 NoSuchAlgorithmException e) {
            throw new RuntimeException("MinIO'ya dosya yüklenirken hata oluştu." + e.getMessage());
        }
    }

    @Override
    public InputStream load(String path) throws IOException {
        try {
            // Asynchronous download
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .build();

            // Get the object asynchronously
            GetObjectResponse response = minioClient.getObject(args);

            // You can optionally handle the response (e.g., logging, etc.)
            System.out.println("File retrieved: " + path);

            // Return the InputStream for the file
            return response;

        } catch (MinioException e) {
            throw new RuntimeException("MinIO'dan dosya yüklenirken hata oluştu.", e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
