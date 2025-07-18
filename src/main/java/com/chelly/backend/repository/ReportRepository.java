package com.chelly.backend.repository;

import com.chelly.backend.models.Report;
import com.chelly.backend.models.enums.ReportStatus;
import com.chelly.backend.models.payload.response.ReportStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query("SELECT new com.chelly.backend.models.payload.response.ReportStats(" +
            "  CAST(COUNT(r.id) AS int), " +
            "  CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.COMPLETED THEN 1 ELSE 0 END) AS int), " +
            "  CAST(CASE " +
            "    WHEN COUNT(r.id) = 0 THEN 0 " +
            "    ELSE CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.COMPLETED THEN 1 ELSE 0 END) AS double) / COUNT(r.id) * 100 " +
            "  END AS int), " +
            "  CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.PENDING THEN 1 ELSE 0 END) AS int), " +
            "  CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.IN_PROGRESS THEN 1 ELSE 0 END) AS int), " +
            "  CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.COMPLETED THEN 1 ELSE 0 END) AS int) " +
            ") FROM Report r WHERE r.user.id = :userId")
    Optional<ReportStats> getUserReportStatsByUserId(@Param("userId") Integer userId);

    @Query("SELECT new com.chelly.backend.models.payload.response.ReportStats(" +
            "  CAST(COUNT(r.id) AS int), " +
            "  CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.COMPLETED THEN 1 ELSE 0 END) AS int), " +
            "  CAST(CASE " +
            "    WHEN COUNT(r.id) = 0 THEN 0 " +
            "    ELSE CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.COMPLETED THEN 1 ELSE 0 END) AS double) / COUNT(r.id) * 100 " +
            "  END AS int), " +
            "  CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.PENDING THEN 1 ELSE 0 END) AS int), " +
            "  CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.IN_PROGRESS THEN 1 ELSE 0 END) AS int), " +
            "  CAST(SUM(CASE WHEN r.currentStatus = com.chelly.backend.models.enums.ReportStatus.COMPLETED THEN 1 ELSE 0 END) AS int) " +
            ") FROM Report r")
    Optional<ReportStats> getReportStats();

    @Query("SELECT r FROM Report r " +
            "WHERE (:keyword IS NULL OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " + // Menggunakan :keyword di r.description
            "AND (:status IS NULL OR r.currentStatus = :status) " +
            "AND r.user.id = :userId")
    List<Report> searchAndFilterReports(
            @Param("keyword") String keyword,
            @Param("status") ReportStatus status,
            @Param("userId") Integer userId
    );
}
