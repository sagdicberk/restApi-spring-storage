package com.repsy.restapi.business.concretes;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.restapi.business.abstracts.PackageMetaService;
import com.repsy.restapi.business.abstracts.PackageService;
import com.repsy.restapi.business.abstracts.StorageService;
import com.repsy.restapi.config.StrategyContext;
import com.repsy.restapi.dto.MetaDto;
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
    public void uploadPackage(MetaDto metaDto,MultipartFile metaFile , MultipartFile packageFile, String packageName, String version) throws IOException {
        // Check if metadata matches the path
        try {
            if (!metaDto.getName().equals(packageName) || !metaDto.getVersion().equals(version)) {
                throw new IllegalArgumentException("Metadata does not match path parameters.");
            }

            // Save metadata
            packageMetaService.saveMeta(metaDto);

            // Save the package file to storage
            String storagePath = packageName + "/" + version + "/" + packageFile.getOriginalFilename();
            storageService.save(storagePath, packageFile.getInputStream());

            // Optionally save the metadata JSON file to storage
            storageService.save(packageName + "/" + version + "/" + metaFile.getOriginalFilename() , packageFile.getInputStream());
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public InputStream downloadPackage(String packageName, String version, String fileName) throws IOException {
        String storagePath = packageName + "/" + version + "/" + fileName;
        return storageService.load(storagePath);
    }
}
