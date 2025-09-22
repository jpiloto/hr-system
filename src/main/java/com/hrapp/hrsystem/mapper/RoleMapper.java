package com.hrapp.hrsystem.mapper;

import com.hrapp.hrsystem.dto.RoleRequestDTO;
import com.hrapp.hrsystem.dto.RoleResponseDTO;
import com.hrapp.hrsystem.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequestDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        return role;
    }

    public RoleResponseDTO toResponseDTO(Role role) {
        return RoleResponseDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}