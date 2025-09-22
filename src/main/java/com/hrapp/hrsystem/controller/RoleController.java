package com.hrapp.hrsystem.controller;

import com.hrapp.hrsystem.dto.RoleRequestDTO;
import com.hrapp.hrsystem.dto.RoleResponseDTO;
import com.hrapp.hrsystem.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO requestDTO) {
        RoleResponseDTO created = roleService.create(requestDTO);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(@PathVariable Long id,
                                                      @Valid @RequestBody RoleRequestDTO requestDTO) {
        RoleResponseDTO updated = roleService.update(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}