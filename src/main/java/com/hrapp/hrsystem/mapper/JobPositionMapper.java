package com.hrapp.hrsystem.mapper;

import com.hrapp.hrsystem.dto.JobPositionRequestDTO;
import com.hrapp.hrsystem.dto.JobPositionResponseDTO;
import com.hrapp.hrsystem.model.JobPosition;
import org.springframework.stereotype.Component;

@Component
public class JobPositionMapper {

    public JobPosition toEntity(JobPositionRequestDTO dto) {
        return JobPosition.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    public JobPositionResponseDTO toResponseDTO(JobPosition entity) {
        return JobPositionResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }
}