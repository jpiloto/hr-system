package com.hrapp.hrsystem.mapper;

import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.event.EmployeeCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class EmployeeCreatedEventMapper {

    public EmployeeCreatedEvent toEvent(EmployeeResponseDTO dto) {
        return new EmployeeCreatedEvent(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getJobTitle(),
                dto.getPhoneNumber(),
                dto.getHireDate(),
                dto.getDepartmentId(),
                dto.getRoleIds(),
                dto.getJobPositionTitle(),
                dto.getJobPositionLevel()
        );
    }
}