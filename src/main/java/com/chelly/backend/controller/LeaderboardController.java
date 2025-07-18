package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.User;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@AllArgsConstructor
public class LeaderboardController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<User>>> getLeaderboard() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "success fetching leaderboard",
                userRepository.findTopUsersForLeaderboard()
        );
    }

    @GetMapping("/current-user")
    public ResponseEntity<SuccessResponse<Integer>> getCurrentUserRank() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "success fetching current user rank",
                userRepository.getUserRank(user.getId())
        );
    }
}
