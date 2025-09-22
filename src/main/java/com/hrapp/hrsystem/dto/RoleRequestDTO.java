package com.hrapp.hrsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestDTO {
    @NotBlank(message = "Role name is required")
    private String name;
}