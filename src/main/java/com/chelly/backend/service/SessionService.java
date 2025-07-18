package com.chelly.backend.service;

import com.chelly.backend.models.User;
import com.chelly.backend.models.UserSession;
import com.chelly.backend.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final Duration cookieExpiration;

    public SessionService(
            SessionRepository sessionRepository,
            @Value("${session.expiration}") Duration cookieExpiration) {
        this.sessionRepository = sessionRepository;
        this.cookieExpiration = cookieExpiration;
    }

    public String createSession(User user) {
        sessionRepository.findByUser(user).ifPresent(sessionRepository::delete);

        String token = UUID.randomUUID().toString();

        sessionRepository.save(UserSession.builder()
                .token(token)
                .user(user)
                .expiresAt(Instant.now().plus(cookieExpiration))
                .build());

        return token;
    }

    public Optional<UserSession> findByToken(String token) {
        return sessionRepository.findByToken(token);
    }

    public Boolean isSessionValid(UserSession userSession) {
        return userSession.getExpiresAt().isAfter(Instant.now());
    }
}
