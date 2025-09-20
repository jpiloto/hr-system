package com.hrapp.hrsystem.repository;

import com.hrapp.hrsystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}