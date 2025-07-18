package com.chelly.backend.models.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor // Crucial for JPA to use a constructor with all arguments in order
@NoArgsConstructor
public class ReportStats {
    private Long totalReports = 0L;
    private Long verifiedReports = 0L;
    private Double accuracy = 0.0; // THIS MUST BE DOUBLE
    private Long pendingReports = 0L;
    private Long inProgressReports = 0L;
    private Long completedReports = 0L;

}
