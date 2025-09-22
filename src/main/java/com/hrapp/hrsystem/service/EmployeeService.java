package com.hrapp.hrsystem.service;

import com.hrapp.hrsystem.dto.EmployeeRequestDTO;
import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.exception.ResourceNotFoundException;
import com.hrapp.hrsystem.mapper.EmployeeMapper;
import com.hrapp.hrsystem.model.Employee;
import com.hrapp.hrsystem.model.JobPosition;
import com.hrapp.hrsystem.repository.EmployeeRepository;
import com.hrapp.hrsystem.repository.JobPositionRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    private final JobPositionRepository jobPositionRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper,
                           JobPositionRepository jobPositionRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.jobPositionRepository = jobPositionRepository;
    }



    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponseDTO)
                .toList();
    }

//    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
//        Employee employee = employeeMapper.toEntity(requestDTO);
//        Employee saved = employeeRepository.save(employee);
//        return employeeMapper.toResponseDTO(saved);
//    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        JobPosition jobPosition = jobPositionRepository.findById(requestDTO.getJobPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("JobPosition not found with id " + requestDTO.getJobPositionId()));

        Employee employee = employeeMapper.toEntity(requestDTO, jobPosition);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(saved);
    }


//    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
//        Employee existing = employeeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
//
//        Employee updated = employeeMapper.toEntity(requestDTO);
//        updated.setId(id); // preserve ID
//        Employee saved = employeeRepository.save(updated);
//        return employeeMapper.toResponseDTO(saved);
//    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        JobPosition jobPosition = jobPositionRepository.findById(requestDTO.getJobPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("JobPosition not found with id " + requestDTO.getJobPositionId()));

        Employee updated = employeeMapper.toEntity(requestDTO, jobPosition);
        updated.setId(id);
        Employee saved = employeeRepository.save(updated);
        return employeeMapper.toResponseDTO(saved);
    }


    public void deleteEmployee(Long id) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        employeeRepository.delete(existing);
    }
}