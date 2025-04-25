package com.repsy.restapi.business.concretes;

import com.repsy.storage.FileSystemStorageService;
import com.repsy.storage.ObjectStorage;
import com.repsy.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
        return switch (storageStrategy.toLowerCase().trim()) {
            case "file-system" -> applicationContext.getBean(FileSystemStorageService.class);
            case "object-storage" -> applicationContext.getBean(ObjectStorage.class);
            default -> throw new IllegalArgumentException("Unsupported storage strategy: " + storageStrategy);
        };
    }
}