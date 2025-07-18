package com.chelly.backend.repository;

import com.chelly.backend.models.User;
import com.chelly.backend.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Integer> {
    Optional<UserSession> findByUser(User user);

    Optional<UserSession> findByToken(String token);
}
