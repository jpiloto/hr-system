package com.hrapp.hrsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPositionRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;
}