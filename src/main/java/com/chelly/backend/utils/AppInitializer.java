package com.chelly.backend.utils;

import com.chelly.backend.models.Image;
import com.chelly.backend.models.Role;
import com.chelly.backend.repository.ImageRepository;
import com.chelly.backend.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class AppInitializer implements CommandLineRunner {
    private final DataSource dataSource;
    private final RoleRepository roleRepository;
    private final ImageRepository imageRepository;

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeBadges();
    }

    void initializeRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder()
                .name("USER")
                .build());
        roles.add(Role.builder()
                .name("ADMIN")
                .build());
        roleRepository.saveAll(roles);
    }

    void initializeBadges() {
        Image newbieIcon = imageRepository.save(Image.builder()
                .name("newbie_reporter_icon")
                .url("https://img.icons8.com/clouds/50/prize.png")
                .type("image/png")
                .build());

        Image activeIcon = imageRepository.save(Image.builder()
                .name("active_reporter_icon")
                .url("https://img.icons8.com/bubbles/50/guarantee.png")
                .type("image/png")
                .build());

        Image veteranIcon = imageRepository.save(Image.builder()
                .name("veteran_reporter_icon")
                .url("https://img.icons8.com/bubbles/50/medal2-1.png")
                .type("image/png")
                .build());

        Image communityHeroIcon = imageRepository.save(Image.builder()
                .name("community_hero_icon")
                .url("https://img.icons8.com/bubbles/50/prize.png")
                .type("image/png")
                .build());

        Image trafficKnightIcon = imageRepository.save(Image.builder()
                .name("traffic_knight_icon")
                .url("https://img.icons8.com/doodle/50/first-place-ribbon--v1.png")
                .type("image/png")
                .build());

//        List<Badge> badges = Arrays.asList(
//                Badge.builder()
//                        .name("Pelapor Pemula")
//                        .description("Diberikan kepada pengguna yang baru bergabung dan membuat laporan pertamanya.")
//                        .image(newbieIcon)
//                        .required_points(0)
//                        .build(),
//
//                Badge.builder()
//                        .name("Pelapor Aktif")
//                        .description("Diberikan setelah berhasil melaporkan 5 pelanggaran yang divalidasi.")
//                        .image(activeIcon)
//                        .required_points(50)
//                        .build(),
//
//                Badge.builder()
//                        .name("Veteran Jalanan")
//                        .description("Diberikan kepada pelapor yang konsisten dengan lebih dari 20 laporan yang divalidasi.")
//                        .image(veteranIcon)
//                        .required_points(200)
//                        .build(),
//
//                Badge.builder()
//                        .name("Pahlawan Komunitas")
//                        .description("Diberikan kepada pengguna dengan total 50 laporan valid dan kontribusi positif.")
//                        .image(communityHeroIcon)
//                        .required_points(500)
//                        .build(),
//
//                Badge.builder()
//                        .name("Ksatria Lalu Lintas")
//                        .description("Penghargaan tertinggi untuk dedikasi luar biasa dalam menjaga ketertiban lalu lintas (100+ laporan valid).")
//                        .image(trafficKnightIcon)
//                        .required_points(1000)
//                        .build()
//        );
//
//        badgeRepository.saveAll(badges);
    }

    void initDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("------------------------------------------------------------------");
            System.out.println("Spring Boot connected to database successfully!");
            System.out.println("Database URL: " + connection.getMetaData().getURL());
            System.out.println("Database User: " + connection.getMetaData().getUserName());
            System.out.println("------------------------------------------------------------------");
        } catch (SQLException e) {
            System.err.println("------------------------------------------------------------------");
            System.err.println("Failed to connect to database!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("------------------------------------------------------------------");
            e.printStackTrace(); // Cetak stack trace untuk detail lebih lanjut
        }
    }
}
