package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.Report;
import com.chelly.backend.models.ReportSearchCriteria;
import com.chelly.backend.models.payload.request.ReportRequest;
import com.chelly.backend.models.payload.request.UpdateReportStatusRequest;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
@Tag(name = "Laporan", description = "API untuk mengelola laporan dari pengguna")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    @Operation(summary = "Ambil Semua Laporan", description = "Mengambil seluruh data laporan yang tersedia (Admin only)")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<Report>>> findAllReport() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil semua laporan",
                reportService.findAll()
        );
    }

    @GetMapping("/search")
    @Operation(
            summary = "Pencarian Laporan",
            description = "Mengambil data laporan berdasarkan kriteria pencarian tertentu. Hanya dapat diakses oleh admin."
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<Report>>> searchReport(ReportSearchCriteria reportSearchCriteria) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil laporan berdasarkan kriteria pencarian",
                reportService.searchReports(reportSearchCriteria)
        );
    }


    @GetMapping("/{id}")
    @Operation(summary = "Ambil Laporan Berdasarkan ID", description = "Mengambil data laporan berdasarkan ID tertentu")
    public ResponseEntity<SuccessResponse<Report>> findById(@PathVariable Integer id) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil laporan berdasarkan ID",
                reportService.findById(id)
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Buat Laporan Baru", description = "Mengirim laporan baru oleh pengguna")
    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")
    public ResponseEntity<SuccessResponse<Report>> createReport(@ModelAttribute @Valid ReportRequest reportRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.CREATED,
                "Berhasil membuat laporan",
                reportService.createReport(reportRequest)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui Status Laporan", description = "Memperbarui status dari laporan berdasarkan ID (Admin only)")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Report>> updateReportStatus(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateReportStatusRequest updateReportStatusRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil memperbarui status laporan",
                reportService.updateReportStatus(id, updateReportStatusRequest)
        );
    }
}
