package com.ChatApp.Utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.lang.SecurityException;

@Component
public class UploadDocument {

    private static final String UPLOAD_DIR = "uploads";
    private static final String PROTECTED_DIR = "protected_uploads";
    

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file cannot be empty.");
        }

        Path uploadDir = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String extension = getFileExtension(originalFilename);

        String uniqueFilename = UUID.randomUUID().toString() + extension;

        Path targetPath = uploadDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }

    private String getFileExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return (index != -1) ? filename.substring(index) : "";
    }

    public boolean deleteFile(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        Path uploadDir = Paths.get(UPLOAD_DIR);
        Path filePath = uploadDir.resolve(fileName).normalize();

        // Prevent path traversal
        if (!filePath.startsWith(uploadDir)) {
            throw new SecurityException("Invalid file path.");
        }

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return true;
        }

        return false; // File not found
    }

    public String uploadProtectedFile(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Protected upload file cannot be empty.");
        }

        Path protectedDir = Paths.get(PROTECTED_DIR);
        if (!Files.exists(protectedDir)) {
            Files.createDirectories(protectedDir);
        }

        // Safely extract extension
        String originalName = file.getOriginalFilename();
        String extension = getProtectedFileExtension(originalName);

        // Always save as: {uuid}.ext
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        Path targetPath = protectedDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename; // Return filename INCLUDING extension
    }

    private String getProtectedFileExtension(String filename) {
    if (filename == null) {
        throw new IllegalArgumentException("Filename is null.");
    }

    String clean = Paths.get(filename).getFileName().toString();
    int lastDot = clean.lastIndexOf('.');

    // No extension -> NOT allowed in your chat system
    if (lastDot == -1 || lastDot == clean.length() - 1) {
        throw new IllegalArgumentException("File must have a valid extension.");
    }

    return clean.substring(lastDot); // e.g. ".png"
}

    public boolean deleteProtectedFile(String fileName) throws IOException {

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        Path protectedDir = Paths.get(PROTECTED_DIR);
        Path filePath = protectedDir.resolve(fileName).normalize();

        // Prevent path traversal attack
        if (!filePath.startsWith(protectedDir)) {
            throw new SecurityException("Invalid protected file path.");
        }

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return true;
        }

        return false;
    }

}
