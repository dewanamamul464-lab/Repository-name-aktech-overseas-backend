package com.aktech.overseas.service;

import com.aktech.overseas.dto.FileUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class FileStorageService {

    private final CloudinaryService cloudinaryService;

    public FileStorageService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    // ==========================
    // Upload File to Cloudinary
    // ==========================
    public FileUploadResponse uploadFile(MultipartFile file) {

        try {

            String cvUrl = cloudinaryService.uploadCv(file);

            return new FileUploadResponse(
                    file.getOriginalFilename(),
                    cvUrl,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "CV upload failed: " + e.getMessage(),
                    e
            );
        }
    }

    // ==========================
    // Return Cloudinary URL
    // ==========================
    public String loadFile(String cvUrl) {

        return cvUrl;
    }
}