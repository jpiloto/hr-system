package com.hrapp.hrsystem.repository;

import com.hrapp.hrsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
}
