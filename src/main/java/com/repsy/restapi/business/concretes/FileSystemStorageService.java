package com.repsy.restapi.business.concretes;

import com.repsy.restapi.business.abstracts.StorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service("file-system")
public class FileSystemStorageService implements StorageService {

    private final Path rootDir = Paths.get("packageRepo");


    @Override
    public void save(String path, InputStream inputStream) throws IOException {
        Path fullPath = rootDir.resolve(path).normalize();
        Files.createDirectories(fullPath.getParent());
        Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public InputStream load(String path) throws IOException {
        Path fullPath = rootDir.resolve(path).normalize();
        return Files.newInputStream(fullPath);
    }
}
