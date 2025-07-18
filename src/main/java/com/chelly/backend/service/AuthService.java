package com.chelly.backend.service;

import com.chelly.backend.models.Role;
import com.chelly.backend.models.User;
import com.chelly.backend.models.exceptions.AuthException;
import com.chelly.backend.models.exceptions.DuplicateElementException;
import com.chelly.backend.models.exceptions.ResourceNotFoundException;
import com.chelly.backend.models.payload.request.LoginRequest;
import com.chelly.backend.models.payload.request.RegisterRequest;
import com.chelly.backend.models.payload.response.AuthResponse;
import com.chelly.backend.repository.RoleRepository;
import com.chelly.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateElementException("Email telah digunakan");
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ADMIN").orElseThrow(
                () -> new ResourceNotFoundException("Role was not found")
        ));


        return userRepository.save(User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .roles(roles)
                .level(1)
                .points(105)
                .build());
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Email tidak ditemukan.")
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException("Email atau password salah");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        String token = sessionService.createSession(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    public void logout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sessionService.findByUser(user).ifPresent(sessionService::deleteSession);
    }
}
