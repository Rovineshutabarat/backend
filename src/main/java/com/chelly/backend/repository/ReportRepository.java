package com.chelly.backend.repository;

import com.chelly.backend.models.Report;
import com.chelly.backend.models.enums.ReportStatus;
import com.chelly.backend.models.payload.response.ReportStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer>, JpaSpecificationExecutor<Report> {
    @Query("SELECT new com.chelly.backend.models.payload.response.ReportStats(" +
            "COUNT(report.id), " +                                                      // Parameter 1: Total laporan
            "SUM(CASE WHEN report.currentStatus = 'COMPLETED' THEN 1 ELSE 0 END), " +  // Parameter 2: Laporan completed (verified)
            "CAST(SUM(CASE WHEN report.currentStatus = 'COMPLETED' THEN 1 ELSE 0 END) * 100.0 / COUNT(report) AS double), " +  // Parameter 3: Persentase akurasi
            "SUM(CASE WHEN report.currentStatus = 'PENDING' THEN 1 ELSE 0 END), " +    // Parameter 4: Laporan pending
            "SUM(CASE WHEN report.currentStatus = 'IN_PROGRESS' THEN 1 ELSE 0 END), " + // Parameter 5: Laporan in progress
            "SUM(CASE WHEN report.currentStatus = 'COMPLETED' THEN 1 ELSE 0 END)) " +  // Parameter 6: Laporan completed
            "FROM Report report WHERE (:userId IS NULL OR report.user.id = :userId)")
    ReportStats getReportStats(@Param("userId") Integer userId);


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
