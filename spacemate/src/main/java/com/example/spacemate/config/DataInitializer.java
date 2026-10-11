package com.example.spacemate.config;

import com.example.spacemate.entity.User;
import com.example.spacemate.enums.Role;
import com.example.spacemate.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail(
                    "admin@spacemate.com")) {

                User admin = new User();

                admin.setName("SpaceMate Admin");

                admin.setEmail(
                        "admin@spacemate.com"
                );

                admin.setPassword(
                        passwordEncoder.encode(
                                "admin123"
                        )
                );

                admin.setRole(Role.ADMIN);

                admin.setPhone("9999999999");

                userRepository.save(admin);
            }
        };
    }
}