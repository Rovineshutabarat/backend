package com.chelly.backend.models.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    @Size(min = 6, max = 50, message = "Nama lengkap harus terdiri dari 6 hingga 50 karakter")
    private String fullName;

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 5, message = "Username minimal 5 karakter")
    @Size(max = 25, message = "Username maksimal 25 karakter")
    private String username;

    @NotBlank(message = "Nomor Hp tidak boleh kosong")
    @Size(max = 20, message = "Nomor telepon maksimal 20 karakter")
    private String phoneNumber;

    @Size(max = 200, message = "Alamat maksimal 200 karakter")
    private String address;

    @Past(message = "Tanggal lahir harus di masa lalu")
    private LocalDateTime birthDate;

    private MultipartFile profilePicture;
}
