package com.hrapp.hrsystem.controller;

import com.hrapp.hrsystem.dto.EmployeeRequestDTO;
import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.event.EmployeeCreatedEvent;
import com.hrapp.hrsystem.producer.EmployeeEventProducer;
import com.hrapp.hrsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeEventProducer eventProducer;


    public EmployeeController(EmployeeService employeeService, EmployeeEventProducer eventProducer) {
        this.employeeService = employeeService;
        this.eventProducer = eventProducer;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmployeeResponseDTO> getEmployees() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());

        return employeeService.getAllEmployees();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
        EmployeeResponseDTO created = employeeService.createEmployee(dto);

        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
                created.getId(),
                created.getName(),
                created.getEmail(),
                created.getJobTitle(),
                created.getPhoneNumber(),
                created.getHireDate(),
                created.getDepartmentId(),
                created.getRoleIds(),
                created.getJobPositionTitle()
        );

        eventProducer.sendEmployeeCreatedEvent(event);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO requestDTO) {
        EmployeeResponseDTO updated = employeeService.updateEmployee(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}