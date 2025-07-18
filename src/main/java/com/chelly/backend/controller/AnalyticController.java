package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.payload.response.ReportStats;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.service.AnalyticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@AllArgsConstructor
@Tag(name = "Analitik", description = "API untuk statistik laporan")
public class AnalyticController {
    private final AnalyticService analyticService;

    @GetMapping
    @Operation(summary = "Ambil Statistik Laporan", description = "Mengambil data statistik dari semua laporan yang telah masuk (Admin only)")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SuccessResponse<ReportStats>> getAllReportStats() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil statistik laporan",
                analyticService.getReportStats(null)
        );
    }
}
