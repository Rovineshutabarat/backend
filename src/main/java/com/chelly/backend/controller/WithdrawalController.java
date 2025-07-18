package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.payload.request.WithdrawalRequest;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/withdrawal")
@AllArgsConstructor
public class WithdrawalController {
    private final WithdrawalService withdrawalService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Map<String, Object>>> processWithdrawal(@RequestBody @Valid WithdrawalRequest withdrawalRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "",
                withdrawalService.processWithdrawal(withdrawalRequest)
        );
    }
}
