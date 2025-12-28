package com.foodflow.filestorage.service.impl;

import com.foodflow.common.exceptions.FileStorageException;
import com.foodflow.filestorage.service.FileStorageService;
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


            return "/uploads/" + folder + "/" + fileName;

        } catch (Exception e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }
}
