package com.repsy.restapi.config;

import com.repsy.restapi.business.abstracts.StorageService;
import com.repsy.restapi.business.concretes.FileSystemStorageService;
import com.repsy.restapi.business.concretes.ObjectStorageServiceImp;
import com.repsy.restapi.model.PackageMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class StrategyContext {

    private final ApplicationContext applicationContext;
    private final String storageStrategy;

    public StrategyContext(ApplicationContext applicationContext,
                                 @Value("${storage.strategy}") String storageStrategy) {
        this.applicationContext = applicationContext;
        this.storageStrategy = storageStrategy;
    }

    public StorageService getStorageService() {
        if ("file-system".equalsIgnoreCase(storageStrategy)) {
            return applicationContext.getBean("file-system", FileSystemStorageService.class);
        } else if ("object-storage".equalsIgnoreCase(storageStrategy)) {
            return applicationContext.getBean("object-storage", ObjectStorageServiceImp.class);
        } else {
            throw new IllegalArgumentException("Unsupported storage strategy: " + storageStrategy);
        }
    }
}