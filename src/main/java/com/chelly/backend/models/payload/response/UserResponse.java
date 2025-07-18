package com.chelly.backend.models.payload.response;

import com.chelly.backend.models.Image;
import com.chelly.backend.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDateTime birthdate;
    private Image image;
    private List<Role> roles;
    private ReportStats reportStats;
    private UserStats stats;
}
