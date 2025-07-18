package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.Report;
import com.chelly.backend.models.ReportSearchCriteria;
import com.chelly.backend.models.User;
import com.chelly.backend.models.payload.request.UpdateProfileRequest;
import com.chelly.backend.models.payload.response.ReportStats;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.models.payload.response.UserResponse;
import com.chelly.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
@Tag(name = "Pengguna", description = "API terkait data dan aktivitas pengguna")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Ambil Data Pengguna Saat Ini", description = "Mengambil data pengguna yang sedang login")
    public ResponseEntity<SuccessResponse<UserResponse>> getCurrentUser() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil data pengguna saat ini",
                userService.getCurrentUser()
        );
    }

    @GetMapping("/report-stats")
    @Operation(summary = "Statistik Laporan Pengguna", description = "Mengambil statistik laporan milik pengguna saat ini")
    public ResponseEntity<SuccessResponse<ReportStats>> getUserReportStats() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil statistik laporan pengguna",
                userService.getUserReportStats()
        );
    }

    @GetMapping("/rank")
    @Operation(summary = "Peringkat Pengguna", description = "Mengambil peringkat pengguna berdasarkan laporan yang dikirimkan")
    public ResponseEntity<SuccessResponse<Integer>> getCurrentUserRank() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil peringkat pengguna",
                userService.getCurrentUserRank(user.getId())
        );
    }

    @GetMapping("/report-search")
    @Operation(summary = "Cari Laporan Pengguna", description = "Mencari laporan milik pengguna berdasarkan kata kunci atau status")
    public ResponseEntity<SuccessResponse<List<Report>>> searchReportsByUser(
            ReportSearchCriteria reportSearchCriteria
    ) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mencari laporan pengguna",
                userService.searchReportsByUser(reportSearchCriteria)
        );
    }

    @GetMapping("/report-history")
    @Operation(
            summary = "Riwayat Laporan Pengguna",
            description = "Mengambil seluruh laporan milik pengguna yang sedang login, termasuk berdasarkan filter pencarian jika ada."
    )
    public ResponseEntity<SuccessResponse<List<Report>>> getUserReportHistory() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil riwayat laporan pengguna",
                userService.getUserReportHistory()
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@userService.canModifyProfile(#id)")
    @Operation(summary = "Perbarui Profil Pengguna", description = "Memperbarui data profil pengguna yang sedang login")
    public ResponseEntity<SuccessResponse<User>> updateProfile(
            @PathVariable Integer id,
            @ModelAttribute @Valid UpdateProfileRequest updateProfileRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil memperbarui profil pengguna",
                userService.updateProfile(id, updateProfileRequest)
        );
    }
}
