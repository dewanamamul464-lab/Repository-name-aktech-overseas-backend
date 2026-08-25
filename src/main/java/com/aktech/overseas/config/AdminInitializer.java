
        package com.aktech.overseas.config;

import com.aktech.overseas.entity.Role;
import com.aktech.overseas.entity.User;
import com.aktech.overseas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:Admin@2026}")
    private String adminPassword;

    @Bean
    CommandLineRunner createOrUpdateAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            User admin = userRepository
                    .findByUsername(adminUsername)
                    .orElse(null);

            // =====================================================
            // CREATE ADMIN IF IT DOES NOT EXIST
            // =====================================================

            if (admin == null) {

                admin = new User();

                admin.setUsername(adminUsername);

                admin.setPassword(
                        passwordEncoder.encode(adminPassword)
                );

                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println(
                        "========================================"
                );

                System.out.println(
                        "ADMIN ACCOUNT CREATED"
                );

                System.out.println(
                        "Username: " + adminUsername
                );

                System.out.println(
                        "========================================"
                );

            } else {

                // =================================================
                // ENSURE EXISTING USER IS ADMIN
                // =================================================

                boolean changed = false;

                if (admin.getRole() != Role.ADMIN) {

                    admin.setRole(Role.ADMIN);

                    changed = true;
                }

                // =================================================
                // RESET ADMIN PASSWORD
                // =================================================

                if (!passwordEncoder.matches(
                        adminPassword,
                        admin.getPassword())) {

                    admin.setPassword(
                            passwordEncoder.encode(
                                    adminPassword
                            )
                    );

                    changed = true;
                }

                if (changed) {

                    userRepository.save(admin);

                    System.out.println(
                            "========================================"
                    );

                    System.out.println(
                            "ADMIN ACCOUNT UPDATED"
                    );

                    System.out.println(
                            "Username: " + adminUsername
                    );

                    System.out.println(
                            "========================================"
                    );

                } else {

                    System.out.println(
                            "ADMIN ACCOUNT VERIFIED"
                    );
                }
            }
        };
    }
}

