package com.embula.embula_backend.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for handling file upload operations.
 * Manages storing files and generating accessible URLs.
 */
public interface FileStorageService {

    /**
     * Saves an uploaded file to the discount images directory.
     *
     * @param file The multipart file to save
     * @return The relative path/URL to access the saved file
     */
    String saveDiscountImage(MultipartFile file);

    /**
     * Deletes a discount image file from storage.
     *
     * @param imageUrl The relative path of the image to delete
     */
    void deleteDiscountImage(String imageUrl);
}

