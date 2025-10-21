package com.hrapp.hrsystem.mapper;

import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.event.EmployeeCreatedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeCreatedEventMapperTest {

    private final EmployeeCreatedEventMapper mapper = new EmployeeCreatedEventMapper();

    @Test
    void shouldMapAllFieldsIncludingJobPositionLevel() {
        EmployeeResponseDTO dto = EmployeeResponseDTO.builder()
                .id(1L)
                .name("Alice Johnson")
                .email("alice@example.com")
                .jobTitle("HR Specialist")
                .phoneNumber("1234567890")
                .hireDate(LocalDate.of(2020, 1, 1))
                .departmentId(10L)
                .roleIds(Set.of(100L, 200L))
                .jobPositionTitle("HR Specialist")
                .jobPositionLevel(1)
                .build();

        EmployeeCreatedEvent event = mapper.toEvent(dto);

        assertEquals(dto.getId(), event.getEmployeeId());
        assertEquals(dto.getName(), event.getName());
        assertEquals(dto.getEmail(), event.getEmail());
        assertEquals(dto.getJobTitle(), event.getJobTitle());
        assertEquals(dto.getPhoneNumber(), event.getPhoneNumber());
        assertEquals(dto.getHireDate(), event.getHireDate());
        assertEquals(dto.getDepartmentId(), event.getDepartmentId());
        assertEquals(dto.getRoleIds(), event.getRoleIds());
        assertEquals(dto.getJobPositionTitle(), event.getJobPositionTitle());
        assertEquals(dto.getJobPositionLevel(), event.getJobPositionLevel()); // ✅ key assertion
    }
}