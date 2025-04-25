package com.repsy.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.restapi.business.abstracts.PackageService;
import com.repsy.restapi.dto.MetaDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

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

            if (!Objects.requireNonNull(metaFile.getOriginalFilename()).endsWith(".json")) {
                return ResponseEntity.badRequest().body("Metadata file must have a .json extension.");
            }

            if (!Objects.requireNonNull(packageFile.getOriginalFilename()).endsWith(".rep")) {
                return ResponseEntity.badRequest().body("Package file must have a .rep extension.");
            }

            // Read metadata
            ObjectMapper objectMapper = new ObjectMapper();
            MetaDto metaDto = objectMapper.readValue(metaFile.getInputStream(), MetaDto.class);

            // Call the service to upload the package and metadata
            packageService.uploadPackage(metaDto, metaFile, packageFile, packageName, version);

            return ResponseEntity.ok("Package uploaded successfully.");
        } catch (IllegalArgumentException e) {
            // Handle specific known exception
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: " + e.getMessage());
        } catch (IOException e) {
            // Handle IO exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            // Handle generic exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading package: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{packageName}/{version}/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadPackage(
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
                    .body(new InputStreamResource(inputStream));

        } catch (IOException e) {
            // Handle IO exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
