package com.hrapp.hrsystem.mapper;

import com.hrapp.hrsystem.dto.EmployeeRequestDTO;
import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.exception.ResourceNotFoundException;
import com.hrapp.hrsystem.model.Department;
import com.hrapp.hrsystem.model.Employee;
import com.hrapp.hrsystem.model.Role;
import com.hrapp.hrsystem.model.JobPosition;
import com.hrapp.hrsystem.repository.DepartmentRepository;
import com.hrapp.hrsystem.repository.JobPositionRepository;
import com.hrapp.hrsystem.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(target = "roleIds", expression = "java(mapRoleIds(employee.getRoles()))")
    @Mapping(source = "jobPosition.title", target = "jobPositionTitle")
    public abstract EmployeeResponseDTO toResponseDTO(Employee employee);

    public Set<Role> mapRoles(Set<Long> roleIds) {
        if (roleIds == null) return Set.of();
        return roleIds.stream()
                .map(id -> {
                    Role role = new Role();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
    }

    public Set<Long> mapRoleIds(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }

    public Employee toEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setJobTitle(dto.getJobTitle());
        employee.setHireDate(dto.getHireDate());
        employee.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            employee.setDepartment(department);
        }

        if (dto.getRoleIds() != null) {
            Set<Role> roles = dto.getRoleIds().stream()
                    .map(id -> roleRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found")))
                    .collect(Collectors.toSet());
            employee.setRoles(roles);
        }

        if (dto.getJobPositionId() != null) {
            JobPosition jobPosition = jobPositionRepository.findById(dto.getJobPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job position not found"));
            employee.setJobPosition(jobPosition);
        }

        return employee;
    }

    public Employee toEntity(EmployeeRequestDTO dto, JobPosition jobPosition) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setJobTitle(dto.getJobTitle());
        employee.setHireDate(dto.getHireDate());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));

        Set<Role> roles = dto.getRoleIds().stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id)))
                .collect(Collectors.toSet());
        employee.setRoles(roles);

        employee.setJobPosition(jobPosition);
        return employee;
    }

}