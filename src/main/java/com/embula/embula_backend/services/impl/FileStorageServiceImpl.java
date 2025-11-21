package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.services.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Implementation of FileStorageService for handling file uploads.
 * Saves discount images to the 'uploads/discounts/' directory.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    // Base directory for all uploads
    private final String uploadDir = "uploads/discounts/";

    public FileStorageServiceImpl() {
        // Create upload directory if it doesn't exist
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created upload directory: " + uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    /**
     * Saves an uploaded discount image file.
     * Generates a unique filename to prevent conflicts.
     *
     * @param file The uploaded file
     * @return Relative path to the saved file (e.g., "/uploads/discounts/uuid_filename.jpg")
     */
    @Override
    public String saveDiscountImage(MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }

            // Get original filename
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new RuntimeException("File must have a name");
            }

            // Generate unique filename to prevent overwrites
            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

            // Create the full file path
            Path targetLocation = Paths.get(uploadDir + uniqueFilename);

            // Copy file to target location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Return relative URL path (accessible via static resources)
            return "/uploads/discounts/" + uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a discount image from storage.
     *
     * @param imageUrl The relative path of the image (e.g., "/uploads/discounts/file.jpg")
     */
    @Override
    public void deleteDiscountImage(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.startsWith("/uploads/discounts/")) {
                // Extract filename from URL
                String filename = imageUrl.substring("/uploads/discounts/".length());
                Path filePath = Paths.get(uploadDir + filename);

                // Delete file if it exists
                Files.deleteIfExists(filePath);
                System.out.println("Deleted discount image: " + filename);
            }
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + e.getMessage());
            // Don't throw exception - deletion failure shouldn't break the application
        }
    }
}

