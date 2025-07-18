package com.chelly.backend.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfiguration {
    private final String CLOUDINARY_CLOUD_NAME;
    private final String CLOUDINARY_API_KEY;
    private final String CLOUDINARY_API_SECRET;

    public CloudinaryConfiguration(
            @Value("${cloudinary.cloud.name}") String CLOUDINARY_CLOUD_NAME,
            @Value("${cloudinary.api.key}") String CLOUDINARY_API_KEY,
            @Value("${cloudinary.api.secret}") String CLOUDINARY_API_SECRET) {
        this.CLOUDINARY_CLOUD_NAME = CLOUDINARY_CLOUD_NAME;
        this.CLOUDINARY_API_KEY = CLOUDINARY_API_KEY;
        this.CLOUDINARY_API_SECRET = CLOUDINARY_API_SECRET;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUDINARY_CLOUD_NAME,
                "api_key", CLOUDINARY_API_KEY,
                "api_secret", CLOUDINARY_API_SECRET
        ));
    }
}
