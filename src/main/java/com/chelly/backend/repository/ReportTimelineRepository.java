package com.chelly.backend.repository;

import com.chelly.backend.models.ReportTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTimelineRepository extends JpaRepository<ReportTimeline, Integer> {
}
