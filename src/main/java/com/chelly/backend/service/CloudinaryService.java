package com.chelly.backend.service;

import com.chelly.backend.models.Image;
import com.chelly.backend.repository.ImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    public Image uploadImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File tidak boleh kosong.");
            }
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Hanya file gambar yang diizinkan.");
            }
            Map<String, Object> params = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true,
                    "folder", "profile_pictures"
            );

            Map<?, ?> upload = cloudinary.uploader().upload(file.getBytes(), params);

            return imageRepository.save(Image.builder()
                    .name(file.getOriginalFilename())
                    .url(upload.get("secure_url").toString())
                    .type(file.getContentType())
                    .build());
        } catch (IOException e) {
            throw new RuntimeException("Gagal mengupload gambar ke Cloudinary: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Terjadi kesalahan saat upload gambar: " + e.getMessage(), e);
        }
    }
}
