package com.hrapp.hrsystem.mapper;

import com.hrapp.hrsystem.dto.EmployeeRequestDTO;
import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.model.Employee;
import com.hrapp.hrsystem.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "department.id", source = "departmentId")
    @Mapping(target = "roles", expression = "java(mapRoles(requestDTO.getRoleIds()))")
    Employee toEntity(EmployeeRequestDTO requestDTO);

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(target = "roleIds", expression = "java(mapRoleIds(employee.getRoles()))")
    EmployeeResponseDTO toResponseDTO(Employee employee);

    default Set<Role> mapRoles(Set<Long> roleIds) {
        if (roleIds == null) return Set.of();
        return roleIds.stream()
                .map(id -> {
                    Role role = new Role();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
    }

    default Set<Long> mapRoleIds(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }
}