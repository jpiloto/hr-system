package com.hrapp.hrsystem.config;

import com.hrapp.hrsystem.model.User;
import com.hrapp.hrsystem.model.Employee;
import com.hrapp.hrsystem.repository.UserRepository;
import com.hrapp.hrsystem.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(UserRepository userRepository,
                                      EmployeeRepository employeeRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed test user
            if (userRepository.findByUsername("testuser").isEmpty()) {
                User user = User.builder()
                        .username("testuser")
                        .password(passwordEncoder.encode("testpass"))
                        .role("ROLE_ADMIN")
                        .build();
                userRepository.save(user);
            }

            // Seed admin user
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("adminpass"))
                        .role("ROLE_ADMIN")
                        .build();
                userRepository.save(admin);
            }

            // Seed employees
            if (employeeRepository.count() == 0) {
                employeeRepository.save(Employee.builder()
                        .name("Alice Johnson")
                        .department("Engineering")
                        .email("alice@company.com")
                        .build());

                employeeRepository.save(Employee.builder()
                        .name("Bob Smith")
                        .department("Human Resources")
                        .email("bob@company.com")
                        .build());

                employeeRepository.save(Employee.builder()
                        .name("Charlie Nguyen")
                        .department("Finance")
                        .email("charlie@company.com")
                        .build());
            }
        };
    }
}