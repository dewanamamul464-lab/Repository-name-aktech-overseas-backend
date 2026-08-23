package com.aktech.overseas.config;

import com.aktech.overseas.entity.Role;
import com.aktech.overseas.entity.User;
import com.aktech.overseas.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User();

                admin.setUsername("admin");

                admin.setPassword(
                        passwordEncoder.encode("Admin@2026")
                );

                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println(
                        "========================================"
                );
                System.out.println(
                        "DEFAULT ADMIN ACCOUNT CREATED"
                );
                System.out.println(
                        "Username: admin"
                );
                System.out.println(
                        "Password: Admin@2026"
                );
                System.out.println(
                        "========================================"
                );

            } else {

                System.out.println(
                        "ADMIN ACCOUNT ALREADY EXISTS"
                );
            }
        };
    }
}