package com.chelly.backend.repository;

import com.chelly.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u ORDER BY u.points DESC limit 100")
    List<User> findTopUsersForLeaderboard();


    @Query("SELECT COUNT(u) + 1 FROM User u WHERE u.points > (SELECT u2.points FROM User u2 WHERE u2.id = :userId)")
    Integer getUserRank(@Param("userId") Integer userId);
}
