package com.hrapp.hrsystem.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String jobTitle;

    private LocalDate hireDate;

    private String phoneNumber;

    private Long departmentId;

    private Set<Long> roleIds;
}