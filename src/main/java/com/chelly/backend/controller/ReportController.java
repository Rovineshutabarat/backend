package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.Report;
import com.chelly.backend.models.payload.request.ReportRequest;
import com.chelly.backend.models.payload.request.UpdateReportStatusRequest;
import com.chelly.backend.models.payload.response.ReportStats;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.service.ReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Report>>> findAllReport() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Success fetch all report.",
                reportService.findAll()
        );
    }

    @GetMapping("/report-stats")
    public ResponseEntity<SuccessResponse<ReportStats>> getReportStats() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Success fetch report stats.",
                reportService.getReportStats()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Report>> findById(@PathVariable Integer id) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Success fetch report by id.",
                reportService.findById(id)
        );
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Report>> createReport(@ModelAttribute @Valid ReportRequest reportRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.CREATED,
                "Berhasil membuat laporan",
                reportService.createReport(reportRequest)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Report>> updateReportStatus(@PathVariable Integer id, @RequestBody @Valid UpdateReportStatusRequest updateReportStatusRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil update status laporan",
                reportService.updateReportStatus(id, updateReportStatusRequest)
        );
    }
}
