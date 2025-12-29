package com.fooddelivery.restaurants.service;

import com.fooddelivery.restaurants.exception.EmptyFileException;
import com.fooddelivery.restaurants.exception.ImageNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageService {
    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public String storeImage(MultipartFile file, String subdirectory) throws IOException {
        if (file.isEmpty()) {
            throw new EmptyFileException("File is empty");
        }
        Path uploadPath = Paths.get(uploadDir, subdirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String fileName = generateUniqueFileName(fileExtension);

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return subdirectory + "/" + fileName;
    }

    public byte[] getImage(String imagePath) throws IOException {
        Path filePath = Paths.get(uploadDir, imagePath);
        if (!Files.exists(filePath)) {
            throw new ImageNotFoundException("Image not found: " + imagePath);
        }
        return Files.readAllBytes(filePath);
    }
    public void deleteImage(String imagePath) throws IOException {
        Path filePath = Paths.get(uploadDir, imagePath);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
    private String generateUniqueFileName(String extension) {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        long timestamp = System.currentTimeMillis();
        return String.format("%d_%s.%s", timestamp, uuid, extension);
    }
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
