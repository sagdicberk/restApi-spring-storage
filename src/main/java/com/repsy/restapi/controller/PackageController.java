package com.repsy.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.restapi.business.abstracts.PackageService;
import com.repsy.restapi.dto.MetaDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    // Upload package endpoint
    @PostMapping(value = "/{packageName}/{version}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestPart("meta") MultipartFile metaFile,
            @RequestPart("package") MultipartFile packageFile) {

        try {

            if (!metaFile.getOriginalFilename().endsWith(".json")) {
                return ResponseEntity.badRequest().body("Metadata file must have a .json extension.");
            }

            if (!packageFile.getOriginalFilename().endsWith(".rep")) {
                return ResponseEntity.badRequest().body("Package file must have a .rep extension.");
            }

            // Read metadata
            ObjectMapper objectMapper = new ObjectMapper();
            MetaDto metaDto = objectMapper.readValue(metaFile.getInputStream(), MetaDto.class);

            // Call the service to upload the package and metadata
            packageService.uploadPackage(metaDto, metaFile, packageFile, packageName, version);

            return ResponseEntity.ok("Package uploaded successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading package: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{packageName}/{version}/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStream> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName) {

        try {
            // Call the service to download the package, getting the InputStream directly
            InputStream inputStream = packageService.downloadPackage(packageName, version, fileName);

            // Set response headers for downloading
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

            // Return the InputStream as the body of the response
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStream);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Optionally, you can return an error message as InputStream
        }
    }

}
