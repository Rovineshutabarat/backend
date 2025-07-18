package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.User;
import com.chelly.backend.models.payload.request.LoginRequest;
import com.chelly.backend.models.payload.request.RegisterRequest;
import com.chelly.backend.models.payload.response.AuthResponse;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
@Tag(name = "Autentikasi", description = "API untuk proses login, registrasi, dan logout pengguna")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrasi Pengguna Baru", description = "Mendaftarkan akun pengguna baru ke dalam sistem")
    public ResponseEntity<SuccessResponse<User>> register(
            @RequestBody @Valid RegisterRequest registerRequest) {
        log.info("Percobaan registrasi pengguna dengan email: {}", registerRequest.getEmail());
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.CREATED,
                "Pengguna berhasil didaftarkan",
                authService.register(registerRequest)
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Login Pengguna", description = "Melakukan otentikasi pengguna dan mengembalikan token Autentikasi")
    public ResponseEntity<SuccessResponse<AuthResponse>> login(
            @RequestBody @Valid LoginRequest loginRequest) {
        log.info("Percobaan login oleh email: {}", loginRequest.getEmail());
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Login berhasil",
                authService.login(loginRequest)
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout Pengguna", description = "Keluar dari sistem dan menghapus token autentikasi")
    public ResponseEntity<SuccessResponse<String>> logout() {
        log.info("Permintaan logout oleh pengguna");
        authService.logout();
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Logout berhasil",
                "Pengguna berhasil logout"
        );
    }

}
