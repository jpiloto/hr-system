package com.hrapp.hrsystem.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreatedEvent {
    private Long employeeId;
    private String name;
    private String email;
    private String jobTitle;
    private String phoneNumber;
    private LocalDate hireDate;
    private Long departmentId;
    private Set<Long> roleIds;
    private String jobPositionTitle;
    private Integer jobPositionLevel;
}

