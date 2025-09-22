package com.hrapp.hrsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPositionResponseDTO {

    private Long id;
    private String title;
    private String description;
}