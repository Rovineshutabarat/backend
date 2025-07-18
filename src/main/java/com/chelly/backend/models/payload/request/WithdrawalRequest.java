package com.chelly.backend.models.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalRequest {
    private Double pointsToExchange;
    private WalletRequest walletRequest;
}
