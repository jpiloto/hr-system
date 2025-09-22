package com.hrapp.hrsystem.config;

import com.hrapp.hrsystem.model.*;
import com.hrapp.hrsystem.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataSeeder {

    private final RoleRepository roleRepository;

    public DataSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner seedData(UserRepository userRepository,
                                      EmployeeRepository employeeRepository,
                                      DepartmentRepository departmentRepository,
                                      JobPositionRepository jobPositionRepository,
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

            // Seed departments
            Department engineering = departmentRepository.save(Department.builder().name("Engineering").build());
            Department hr = departmentRepository.save(Department.builder().name("Human Resources").build());
            Department finance = departmentRepository.save(Department.builder().name("Finance").build());

            // Seed job positions
            JobPosition softwareEngineer = jobPositionRepository.save(
                    JobPosition.builder()
                            .title("Software Engineer")
                            .description("Develops and maintains backend systems")
                            .build()
            );

            JobPosition hrSpecialist = jobPositionRepository.save(
                    JobPosition.builder()
                            .title("HR Specialist")
                            .description("Manages employee relations and recruitment")
                            .build()
            );

            JobPosition financialAnalyst = jobPositionRepository.save(
                    JobPosition.builder()
                            .title("Financial Analyst")
                            .description("Analyzes financial data and prepares reports")
                            .build()
            );

            // Seed employees
            if (employeeRepository.count() == 0) {
                employeeRepository.save(Employee.builder()
                        .name("Alice Johnson")
                        .email("alice@company.com")
                        .jobTitle("Software Engineer")
                        .hireDate(LocalDate.of(2022, 5, 10))
                        .phoneNumber("+1 (555) 123-4567")
                        .department(engineering)
                        .jobPosition(softwareEngineer)
                        .build());

                employeeRepository.save(Employee.builder()
                        .name("Bob Smith")
                        .email("bob@company.com")
                        .jobTitle("HR Specialist")
                        .hireDate(LocalDate.of(2021, 3, 15))
                        .phoneNumber("+1 (555) 987-6543")
                        .department(hr)
                        .jobPosition(hrSpecialist)
                        .build());

                employeeRepository.save(Employee.builder()
                        .name("Charlie Nguyen")
                        .email("charlie@company.com")
                        .jobTitle("Financial Analyst")
                        .hireDate(LocalDate.of(2023, 1, 20))
                        .phoneNumber("+1 (555) 555-1212")
                        .department(finance)
                        .jobPosition(financialAnalyst)
                        .build());
            }
        };
    }

    @PostConstruct
    public void seedRoles() {
        List<String> defaultRoles = List.of("ADMIN", "HR_MANAGER", "EMPLOYEE");

        for (String roleName : defaultRoles) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Seeded role: " + roleName);
            }
        }
    }
}