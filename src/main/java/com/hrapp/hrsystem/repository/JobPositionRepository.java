package com.hrapp.hrsystem.repository;

import com.hrapp.hrsystem.model.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {
}
