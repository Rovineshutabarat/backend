package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.User;
import com.chelly.backend.models.payload.request.LoginRequest;
import com.chelly.backend.models.payload.request.RegisterRequest;
import com.chelly.backend.models.payload.response.AuthResponse;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<User>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.CREATED,
                "registrasi sukses",
                authService.register(registerRequest)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "berhasil login",
                authService.login(loginRequest)
        );
    }
}
