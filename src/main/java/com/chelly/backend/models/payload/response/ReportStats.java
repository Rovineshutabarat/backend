package com.chelly.backend.models.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportStats {
    private Integer totalReports;
    private Integer verifiedReports;
    private Integer accuracy;
    private Integer pendingReports;
    private Integer inProgressReports;
    private Integer completedReports;
}
