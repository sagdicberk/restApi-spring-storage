package com.repsy.restapi.business.abstracts;

import com.repsy.restapi.dto.MetaDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface PackageService {

    void uploadPackage(MetaDto metaDto,MultipartFile metaFile, MultipartFile packageFile, String packageName, String version) throws IOException;

    InputStream downloadPackage(String packageName, String version, String fileName) throws IOException;
}
