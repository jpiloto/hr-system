package com.hrapp.hrsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;
    private String name;
    private String email;

    // Flattened department info
    private Long departmentId;
    private String departmentName;
}