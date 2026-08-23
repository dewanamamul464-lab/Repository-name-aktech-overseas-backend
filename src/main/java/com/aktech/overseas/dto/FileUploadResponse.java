package com.aktech.overseas.dto;

import java.time.LocalDateTime;

public class FileUploadResponse {

    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;

    public FileUploadResponse() {
    }

    public FileUploadResponse(String fileName, String filePath, LocalDateTime uploadedAt) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}