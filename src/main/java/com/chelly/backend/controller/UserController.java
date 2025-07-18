package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.Report;
import com.chelly.backend.models.User;
import com.chelly.backend.models.enums.ReportStatus;
import com.chelly.backend.models.payload.request.UpdateProfileRequest;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.models.payload.response.ReportStats;
import com.chelly.backend.models.payload.response.UserResponse;
import com.chelly.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse<UserResponse>> getCurrentUser() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Success get current user",
                userService.getCurrentUser()
        );
    }

    @GetMapping("/report-stats")
    public ResponseEntity<SuccessResponse<ReportStats>> getUserReportStats() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Success get current user report stats",
                userService.getUserReportStats()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<Report>>> searchReportsByUser(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ReportStatus reportStatus
    ) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Success search report by user",
                userService.searchReportsByUser(keyword, reportStatus)
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@userService.canModifyProfile(#id)")
    public ResponseEntity<SuccessResponse<User>> updateProfile(
            @PathVariable Integer id,
            @ModelAttribute @Valid UpdateProfileRequest updateProfileRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Sukses update profil pengguna",
                userService.updateProfile(id, updateProfileRequest)
        );
    }
}
