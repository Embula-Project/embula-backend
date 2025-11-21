package com.embula.embula_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration for serving static files.
 * Configures access to uploaded discount images.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure resource handlers to serve uploaded files.
     * This makes files in 'uploads/' directory accessible via HTTP.
     *
     * For example:
     * - File: uploads/discounts/image.jpg
     * - URL: http://localhost:8081/uploads/discounts/image.jpg
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from uploads directory
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Also serve standard static resources from classpath
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}

