package com.hrapp.hrsystem.service;

import com.hrapp.hrsystem.dto.RoleRequestDTO;
import com.hrapp.hrsystem.dto.RoleResponseDTO;
import com.hrapp.hrsystem.exception.ResourceNotFoundException;
import com.hrapp.hrsystem.mapper.RoleMapper;
import com.hrapp.hrsystem.model.Role;
import com.hrapp.hrsystem.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleResponseDTO> getAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponseDTO)
                .toList();
    }

    public RoleResponseDTO getById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id));
        return roleMapper.toResponseDTO(role);
    }

    public RoleResponseDTO create(RoleRequestDTO requestDTO) {
        Role role = roleMapper.toEntity(requestDTO);
        Role saved = roleRepository.save(role);
        return roleMapper.toResponseDTO(saved);
    }

    public RoleResponseDTO update(Long id, RoleRequestDTO requestDTO) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id));
        existing.setName(requestDTO.getName());
        Role saved = roleRepository.save(existing);
        return roleMapper.toResponseDTO(saved);
    }

    public void delete(Long id) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id));
        roleRepository.delete(existing);
    }
}