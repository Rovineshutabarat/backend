package com.chelly.backend.service;

import com.chelly.backend.models.User;
import com.chelly.backend.models.Wallet;
import com.chelly.backend.models.enums.WalletName;
import com.chelly.backend.models.exceptions.DuplicateElementException;
import com.chelly.backend.models.exceptions.ResourceNotFoundException;
import com.chelly.backend.models.payload.request.WalletRequest;
import com.chelly.backend.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public List<Wallet> getCurrentUserBankAccounts() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return walletRepository.findAllByUser(user);
    }

    public Wallet findByAccountNumber(String accountNumber) {
        return walletRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Bank account was not found")
        );
    }

    public Wallet findBankAccountById(Integer id) {
        return walletRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Bank account not found with id: " + id)
        );
    }

    public Wallet createBankAccount(WalletRequest walletRequest) {
        if (walletRepository.existsByAccountNumber(walletRequest.getAccountNumber())) {
            throw new DuplicateElementException("Bank account already exists");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return walletRepository.save(Wallet.builder()
                .walletName(WalletName.valueOf(walletRequest.getWalletName()))
                .accountNumber(walletRequest.getAccountNumber())
                .accountHolderName(walletRequest.getAccountHolderName())
                .isDefault(walletRequest.getIsDefault())
                .user(user)
                .build());
    }

    public Wallet updateBankAccount(Integer id, WalletRequest walletRequest) {
        findBankAccountById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return walletRepository.save(Wallet.builder()
                .id(id)
                .walletName(WalletName.valueOf(walletRequest.getWalletName()))
                .accountNumber(walletRequest.getAccountNumber())
                .accountHolderName(walletRequest.getAccountHolderName())
                .isDefault(walletRequest.getIsDefault())
                .user(user)
                .build());
    }

    public Wallet deleteBankAccount(Integer id) {
        Wallet wallet = findBankAccountById(id);
        walletRepository.delete(wallet);
        return wallet;
    }

    public Wallet setDefaultBankAccount(Integer id) {
        Wallet wallet = findBankAccountById(id);
        wallet.setIsDefault(true);
        return walletRepository.save(wallet);
    }

    public Boolean canModifyBankAccount(Integer id) {
        Wallet wallet = findBankAccountById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return wallet.getUser().getId().equals(user.getId());
    }
}
