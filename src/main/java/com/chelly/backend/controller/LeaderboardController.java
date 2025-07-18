package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.User;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@AllArgsConstructor
@Tag(name = "Leaderboard", description = "API untuk menampilkan Leaderboard")
public class LeaderboardController {
    private final UserRepository userRepository;


    @GetMapping
    @Operation(summary = "Ambil Data Leaderboard", description = "Mengambil daftar pengguna dengan peringkat tertinggi")
    public ResponseEntity<SuccessResponse<List<User>>> getLeaderboard() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Berhasil mengambil data Leaderboard",
                userRepository.findTopUsersForLeaderboard()
        );
    }

}
