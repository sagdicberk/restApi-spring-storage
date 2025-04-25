package com.repsy.restapi.business.concretes;

import com.repsy.restapi.business.abstracts.PackageMetaService;
import com.repsy.restapi.business.abstracts.PackageService;
import com.repsy.restapi.dto.MetaDto;
import com.repsy.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Transactional(rollbackFor = Exception.class)
@Service
public class PackageServiceImp implements PackageService {

    private final PackageMetaService packageMetaService;
    private final StorageService storageService;

    public PackageServiceImp(PackageMetaService packageMetaService, StrategyContext strategyContext) {
        this.packageMetaService = packageMetaService;
        this.storageService = strategyContext.getStorageService();
    }

    @Override
    public void uploadPackage(MetaDto metaDto, MultipartFile metaFile, MultipartFile packageFile, String packageName, String version) throws IOException {
        // Check if metadata matches the path
        try {
            if (!metaDto.getName().equals(packageName) || !metaDto.getVersion().equals(version)) {
                throw new IllegalArgumentException("Metadata name or version does not match the package path parameters. " +
                        "Package name: " + packageName + ", Version: " + version + ", Metadata name: " + metaDto.getName() + ", Version: " + metaDto.getVersion());
            }

            // Save metadata
            packageMetaService.saveMeta(metaDto);

            // Save the package file to storage
            String storagePath = packageName + "/" + version + "/" + packageFile.getOriginalFilename();
            storageService.save(storagePath, packageFile.getInputStream());

            // Optionally save the metadata JSON file to storage
            String metaStoragePath = packageName + "/" + version + "/" + metaFile.getOriginalFilename();
            storageService.save(metaStoragePath, metaFile.getInputStream());

        } catch (IllegalArgumentException e) {
            // Handling invalid metadata
            throw new IllegalArgumentException("Metadata validation failed: " + e.getMessage(), e);
        } catch (IOException e) {
            // Handling file-related exceptions (e.g., file write/read errors)
            throw new IOException("Error occurred while saving the package files: " + e.getMessage(), e);
        } catch (Exception e) {
            // Catch any other general exception
            throw new RuntimeException("An unexpected error occurred during package upload: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream downloadPackage(String packageName, String version, String fileName) throws IOException {
        String storagePath = packageName + "/" + version + "/" + fileName;
        try {
            return storageService.load(storagePath);
        } catch (IOException e) {
            throw new IOException("Error occurred while downloading the package: " + e.getMessage(), e);
        }
    }
}
