package com.chelly.backend.service;

import com.chelly.backend.models.payload.response.ReportStats;
import com.chelly.backend.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnalyticService {
    private final ReportRepository reportRepository;

    //    find report stats by optional user
//    admin only
    public ReportStats getReportStats(Integer userId) {
        return reportRepository.getReportStats(userId);
    }

//    filter created reports weekly


}
