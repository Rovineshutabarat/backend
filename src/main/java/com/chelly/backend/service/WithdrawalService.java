package com.chelly.backend.service;

import com.chelly.backend.models.User;
import com.chelly.backend.models.Wallet;
import com.chelly.backend.models.Withdrawal;
import com.chelly.backend.models.enums.WalletName;
import com.chelly.backend.models.enums.WithdrawalStatus;
import com.chelly.backend.models.exceptions.WithdrawalException;
import com.chelly.backend.models.payload.request.WithdrawalRequest;
import com.chelly.backend.repository.UserRepository;
import com.chelly.backend.repository.WithdrawalRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    private final WalletService walletService;
    private final UserRepository userRepository;
    private final MidtransService midtransService;

    private static final double POINTS_TO_IDR_RATIO = 100.0;
    private static final double MIN_DISBURSEMENT_AMOUNT_IDR = 10000.0;

    @Transactional
    public Map<String, Object> processWithdrawal(WithdrawalRequest withdrawalRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Wallet wallet = walletService.findByAccountNumber(withdrawalRequest.getWalletRequest().getAccountNumber());

        if (!wallet.getUser().getId().equals(user.getId())) {
            throw new WithdrawalException("Rekening bank/e-wallet yang dipilih tidak dimiliki oleh pengguna ini.");
        }

        double amountInIdr = withdrawalRequest.getPointsToExchange() * POINTS_TO_IDR_RATIO;

        if (amountInIdr < MIN_DISBURSEMENT_AMOUNT_IDR) {
            throw new WithdrawalException("Jumlah penarikan minimal adalah Rp " + MIN_DISBURSEMENT_AMOUNT_IDR + ". Anda saat ini hanya akan mendapatkan Rp " + amountInIdr);
        }

        if (user.getPoints() < withdrawalRequest.getPointsToExchange()) {
            throw new WithdrawalException("Saldo poin Anda tidak mencukupi untuk penarikan ini.");
        }

        user.setPoints(user.getPoints() - withdrawalRequest.getPointsToExchange().intValue());
        userRepository.save(user);

        String externalId = "WDL-" + UUID.randomUUID();

        Withdrawal withdrawal = Withdrawal.builder()
                .user(user)
                .amount(amountInIdr)
                .pointsExchanged(withdrawalRequest.getPointsToExchange())
                .withdrawalStatus(WithdrawalStatus.PENDING)
                .wallet(wallet)
                .build();
        withdrawal = withdrawalRepository.save(withdrawal);

        Map<String, Object> midtransResponse;
        try {
            // Hanya proses GoPay
            if (!WalletName.GOPAY.name().equalsIgnoreCase(withdrawalRequest.getWalletRequest().getWalletName())) {
                throw new WithdrawalException("Saat ini hanya GoPay yang didukung untuk penarikan.");
            }

            midtransResponse = midtransService.createGopayCharge(
                    externalId,
                    amountInIdr
            );

            String transactionStatus = (String) midtransResponse.get("transaction_status");
            if ("pending".equalsIgnoreCase(transactionStatus)) {
                withdrawal.setWithdrawalStatus(WithdrawalStatus.PENDING);
            } else {
                withdrawal.setWithdrawalStatus(WithdrawalStatus.REJECTED);
                user.setPoints(user.getPoints() + withdrawalRequest.getPointsToExchange().intValue());
                userRepository.save(user);
            }
            withdrawalRepository.save(withdrawal);

        } catch (Exception e) {
            user.setPoints(user.getPoints() + withdrawalRequest.getPointsToExchange().intValue());
            userRepository.save(user);
            withdrawal.setWithdrawalStatus(WithdrawalStatus.REJECTED);
            withdrawalRepository.save(withdrawal);
            throw new WithdrawalException("Penarikan gagal: " + e.getMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Permintaan penarikan berhasil diajukan. Status: " + withdrawal.getWithdrawalStatus());
        response.put("withdrawalId", withdrawal.getId());
        response.put("midtransResponse", midtransResponse);
        return response;
    }
}
