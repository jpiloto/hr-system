package com.hrapp.hrsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {
    private Long id;
    private String name;
}