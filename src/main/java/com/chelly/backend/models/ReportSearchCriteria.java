package com.chelly.backend.models;

import com.chelly.backend.models.enums.ReportCategory;
import com.chelly.backend.models.enums.ReportStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ReportSearchCriteria {

    private String keyword;

    private ReportCategory reportCategory;

    private ReportStatus reportStatus;

    private Integer userId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
}
