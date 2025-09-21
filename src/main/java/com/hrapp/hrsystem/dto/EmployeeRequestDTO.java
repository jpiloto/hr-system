package com.hrapp.hrsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;

    @Pattern(
            regexp = "^\\+?[0-9. ()-]{7,25}$",
            message = "Phone number format is invalid"
    )
    private String phoneNumber;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    private Set<Long> roleIds;
}