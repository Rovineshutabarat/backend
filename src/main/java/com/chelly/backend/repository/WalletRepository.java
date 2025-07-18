package com.chelly.backend.repository;

import com.chelly.backend.models.Wallet;
import com.chelly.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Boolean existsByAccountNumber(String accountNumber);

    Optional<Wallet> findByAccountNumber(String accountNumber);

    List<Wallet> findAllByUser(User user);
}
