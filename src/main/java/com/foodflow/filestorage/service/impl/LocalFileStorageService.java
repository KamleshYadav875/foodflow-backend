package com.foodflow.filestorage.service.impl;

import com.foodflow.filestorage.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(LocalFileStorageService.class);
    @Value("${app.storage.location}")
    private String basePath;

    @Override
    public String upload(MultipartFile file, String folder) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            Path uploadPath = Paths.get(basePath, folder).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Saved file at: " + filePath.toAbsolutePath());

            return "/uploads/" + folder + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
