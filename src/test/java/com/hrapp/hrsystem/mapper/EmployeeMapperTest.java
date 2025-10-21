package com.hrapp.hrsystem.mapper;

import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.model.Department;
import com.hrapp.hrsystem.model.Employee;
import com.hrapp.hrsystem.model.JobPosition;
import com.hrapp.hrsystem.model.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    @Test
    void shouldMapJobPositionLevelToResponseDTO() {
        JobPosition jobPosition = JobPosition.builder()
                .title("HR Specialist")
                .description("Handles HR tasks")
                .level(1)
                .build();

        Department department = new Department();
        department.setId(10L);

        Role role1 = new Role();
        role1.setId(100L);
        Role role2 = new Role();
        role2.setId(200L);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Alice Johnson");
        employee.setEmail("alice@example.com");
        employee.setJobTitle("HR Specialist");
        employee.setHireDate(LocalDate.of(2020, 1, 1));
        employee.setPhoneNumber("1234567890");
        employee.setDepartment(department);
        employee.setRoles(Set.of(role1, role2));
        employee.setJobPosition(jobPosition);

        EmployeeResponseDTO dto = mapper.toResponseDTO(employee);

        assertEquals(1L, dto.getId());
        assertEquals("Alice Johnson", dto.getName());
        assertEquals("alice@example.com", dto.getEmail());
        assertEquals("HR Specialist", dto.getJobTitle());
        assertEquals(LocalDate.of(2020, 1, 1), dto.getHireDate());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals(10L, dto.getDepartmentId());
        assertEquals(Set.of(100L, 200L), dto.getRoleIds());
        assertEquals("HR Specialist", dto.getJobPositionTitle());
        assertEquals(1, dto.getJobPositionLevel()); // ✅ key assertion
    }
}