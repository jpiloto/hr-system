package com.hrapp.hrsystem.service;

import com.hrapp.hrsystem.dto.EmployeeDTO;
import com.hrapp.hrsystem.model.Employee;
import com.hrapp.hrsystem.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(emp -> EmployeeDTO.builder()
                        .id(emp.getId())
                        .name(emp.getName())
                        .email(emp.getEmail())
                        .departmentId(emp.getDepartment() != null ? emp.getDepartment().getId() : null)
                        .departmentName(emp.getDepartment() != null ? emp.getDepartment().getName() : null)
                        .build())
                .collect(Collectors.toList());
    }

}