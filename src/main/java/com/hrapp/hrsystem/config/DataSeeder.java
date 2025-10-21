package com.hrapp.hrsystem.config;

import com.hrapp.hrsystem.dto.EmployeeRequestDTO;
import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.event.EmployeeCreatedEvent;
import com.hrapp.hrsystem.mapper.EmployeeCreatedEventMapper;
import com.hrapp.hrsystem.model.*;
import com.hrapp.hrsystem.producer.EmployeeEventProducer;
import com.hrapp.hrsystem.repository.*;
import com.hrapp.hrsystem.service.EmployeeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.awaitility.Awaitility.await;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final RoleRepository roleRepository;
    private final EmployeeEventProducer eventProducer;
    private final EmployeeService employeeService;
    private final EmployeeCreatedEventMapper eventMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Environment environment;


    @Bean
    @Transactional
    public CommandLineRunner seedData(UserRepository userRepository,
                                      EmployeeRepository employeeRepository,
                                      DepartmentRepository departmentRepository,
                                      JobPositionRepository jobPositionRepository,
                                      PasswordEncoder passwordEncoder,
                                      KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            // Smart environment detection
            boolean isLocalProfile = Arrays.asList(environment.getActiveProfiles()).contains("local");
            String kafkaHost = environment.getProperty("spring.kafka.bootstrap-servers");
            boolean isLocalKafka = kafkaHost != null && kafkaHost.contains("localhost");

            log.info("Active profiles: {}", Arrays.toString(environment.getActiveProfiles()));
            log.info("Kafka bootstrap servers: {}", kafkaHost);

            if (!(isLocalProfile || isLocalKafka)) {
                try {
                    await()
                            .atMost(10, TimeUnit.SECONDS)
                            .pollInterval(1, TimeUnit.SECONDS)
                            .until(() -> {
                                try {
                                    kafkaTemplate.send("employee.created", "ping").get();
                                    return true;
                                } catch (Exception e) {
                                    log.warn("Kafka ping failed: {}", e.getMessage());
                                    return false;
                                }
                            });

                    log.info("Kafka is ready. Proceeding with data seeding...");
                } catch (Exception e) {
                    log.warn("Kafka not ready after Awaitility timeout. Proceeding anyway.");
                }
            } else {
                log.info("Local dev detected. Skipping Kafka Awaitility.");
            }


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
                            .level(4)
                            .department(engineering)
                            .build()
            );

            JobPosition hrSpecialist = jobPositionRepository.save(
                    JobPosition.builder()
                            .title("HR Specialist")
                            .description("Manages employee relations and recruitment")
                            .level(2)
                            .department(hr)
                            .build()
            );

            JobPosition financialAnalyst = jobPositionRepository.save(
                    JobPosition.builder()
                            .title("Financial Analyst")
                            .description("Analyzes financial data and prepares reports")
                            .level(3)
                            .department(finance)
                            .build()
            );

            // Seed employees
            if (employeeRepository.count() == 0) {
                List<EmployeeRequestDTO> employeesToSeed = List.of(
                        new EmployeeRequestDTO(
                                "Alice Johnson",
                                "alice@company.com",
                                "Software Engineer",
                                LocalDate.of(2022, 5, 10),
                                "+1 (555) 123-4567",
                                engineering.getId(),
                                Set.of(1L, 2L),
                                softwareEngineer.getId()
                        ),
                        new EmployeeRequestDTO(
                                "Bob Smith",
                                "bob@company.com",
                                "HR Specialist",
                                LocalDate.of(2021, 3, 15),
                                "+1 (555) 987-6543",
                                hr.getId(),
                                Set.of(3L),
                                hrSpecialist.getId()
                        ),
                        new EmployeeRequestDTO(
                                "Charlie Nguyen",
                                "charlie@company.com",
                                "Financial Analyst",
                                LocalDate.of(2023, 1, 20),
                                "+1 (555) 555-1212",
                                finance.getId(),
                                Set.of(3L),
                                financialAnalyst.getId()
                        )
                );

                for (EmployeeRequestDTO dto : employeesToSeed) {
                    EmployeeResponseDTO created = employeeService.createEmployee(dto);
                    EmployeeCreatedEvent event = eventMapper.toEvent(created);
                    eventProducer.sendEmployeeCreatedEvent(event);
                    log.info("Seeded employee and published event for: {}", created.getName());
                }
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
                log.info("Seeded role: {}", roleName);
            }
        }
    }
}