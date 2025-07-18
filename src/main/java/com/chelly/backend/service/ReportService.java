package com.chelly.backend.service;

import com.chelly.backend.models.Report;
import com.chelly.backend.models.ReportSearchCriteria;
import com.chelly.backend.models.ReportTimeline;
import com.chelly.backend.models.User;
import com.chelly.backend.models.enums.ReportCategory;
import com.chelly.backend.models.enums.ReportStatus;
import com.chelly.backend.models.exceptions.ResourceNotFoundException;
import com.chelly.backend.models.payload.request.ReportRequest;
import com.chelly.backend.models.payload.request.UpdateReportStatusRequest;
import com.chelly.backend.models.specifications.ReportSpecification;
import com.chelly.backend.repository.ReportRepository;
import com.chelly.backend.repository.ReportTimelineRepository;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportTimelineRepository reportTimelineRepository;
    private final UserService userService;

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report findById(Integer id) {
        return reportRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Report not found with id: " + id)
        );
    }

    public List<Report> searchReports(ReportSearchCriteria criteria) {
        return reportRepository.findAll(ReportSpecification.getSpecification(criteria));
    }

    public Report createReport(ReportRequest reportRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Report report = Report.builder()
                .reportCategory(ReportCategory.valueOf(reportRequest.getReportCategory()))
                .description(reportRequest.getDescription())
                .location(reportRequest.getLocation())
                .currentStatus(ReportStatus.PENDING)
                .reportDate(reportRequest.getReportDate())
                .user(user)
                .build();

        reportRepository.save(report);

        ReportTimeline initialTimeline = ReportTimeline.builder()
                .report(report)
                .reportStatus(ReportStatus.PENDING)
                .reportDate(reportRequest.getReportDate())
                .build();

        reportTimelineRepository.save(initialTimeline);

        userService.updateStats(user, ReportStatus.PENDING);
        return report;
    }

    public Report updateReportStatus(Integer id, UpdateReportStatusRequest updateReportStatusRequest) {
        Report report = reportRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Report with id " + id + " was not found")
        );

        ReportStatus reportStatus = ReportStatus.valueOf(updateReportStatusRequest.getReportStatus());

        if (report.getCurrentStatus().equals(reportStatus)) {
            throw new ValidationException("Status laporan sudah " + reportStatus.name() + ". Tidak ada perubahan yang diperlukan.");
        }

        boolean statusAlreadyExistsInTimeline = report.getReportTimelines().stream()
                .anyMatch(timeline -> timeline.getReportStatus().equals(reportStatus));

        if (statusAlreadyExistsInTimeline) {
            throw new ValidationException("Status '" + reportStatus.name() + "' sudah ada di riwayat timeline laporan ini.");
        }

        User user = report.getUser();
        userService.updateStats(user, reportStatus);
        report.setCurrentStatus(reportStatus);

        reportTimelineRepository.save(ReportTimeline.builder()
                .reportStatus(reportStatus)
                .report(report)
                .reportDate(LocalDateTime.now())
                .build());

        return reportRepository.save(report);
    }
}
