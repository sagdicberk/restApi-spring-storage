package com.repsy.restapi.business.abstracts;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
    void save(String path, InputStream inputStream) throws IOException;
    InputStream load(String path) throws IOException;
}
