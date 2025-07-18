package com.chelly.backend.models.payload.request;

import com.chelly.backend.models.annotations.EnumValue;
import com.chelly.backend.models.enums.ReportCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    @NotBlank(message = "Kategori laporan tidak boleh kosong")
    @EnumValue(enumClass = ReportCategory.class, message = "Kategori laporan tidak valid. Pilihan: TRAFFIC_VIOLATION, ROAD_DAMAGE, TRAFFIC_SIGN, ACCIDENT, OTHERS")
    private String reportCategory;

    @NotBlank(message = "Deskripsi laporan tidak boleh kosong")
    @Size(min = 10, max = 2000, message = "Deskripsi laporan harus antara 10 sampai 2000 karakter")
    private String description;

    @NotBlank(message = "Alamat laporan tidak boleh kosong")
    @Size(min = 5, max = 500, message = "Alamat laporan harus antara 5 sampai 500 karakter")
    private String location;

    @NotNull(message = "Tanggal dan waktu laporan tidak boleh kosong")
    @PastOrPresent(message = "Tanggal dan waktu laporan tidak boleh di masa depan")
    private LocalDateTime reportDate;
}
