package com.chelly.backend.models.payload.request;

import com.chelly.backend.models.annotations.EnumValue;
import com.chelly.backend.models.enums.ReportCategory;
import com.chelly.backend.models.enums.ReportStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReportStatusRequest {
    @NotBlank(message = "Status laporan tidak boleh kosong")
    @EnumValue(enumClass = ReportStatus.class, message = "Status laporan tidak valid. Pilihan: PENDING, IN_PROGRESS, COMPLETED, REJECTED, CLOSED")
    private String reportStatus;
}
