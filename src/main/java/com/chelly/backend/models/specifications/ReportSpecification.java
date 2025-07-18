package com.chelly.backend.models.specifications;

import com.chelly.backend.models.Report;
import com.chelly.backend.models.ReportSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReportSpecification {

    public static Specification<Report> getSpecification(ReportSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getKeyword() != null && !criteria.getKeyword().isBlank()) {
                String keyword = "%" + criteria.getKeyword().toLowerCase() + "%";
                Predicate descPredicate = cb.like(cb.lower(root.get("description")), keyword);
                Predicate locationPredicate = cb.like(cb.lower(root.get("location")), keyword);
                predicates.add(cb.or(descPredicate, locationPredicate));
            }

            if (criteria.getReportCategory() != null) {
                predicates.add(cb.equal(root.get("reportCategory"), criteria.getReportCategory()));
            }

            if (criteria.getReportStatus() != null) {
                predicates.add(cb.equal(root.get("currentStatus"), criteria.getReportStatus()));
            }

            if (criteria.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), criteria.getUserId()));
            }

            if (criteria.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("reportDate"), criteria.getStartDate()));
            }

            if (criteria.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("reportDate"), criteria.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
