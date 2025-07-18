package com.chelly.backend.models.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStats {
    private Integer level;
    private Integer points;
    private Integer requiredPoints;
    private Integer requiredPointsPercentage;
}
