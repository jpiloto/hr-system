package com.hrapp.hrsystem.controller;

import com.hrapp.hrsystem.dto.JobPositionRequestDTO;
import com.hrapp.hrsystem.dto.JobPositionResponseDTO;
import com.hrapp.hrsystem.service.JobPositionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-positions")
public class JobPositionController {

    private final JobPositionService jobPositionService;

    public JobPositionController(JobPositionService jobPositionService) {
        this.jobPositionService = jobPositionService;
    }

    @GetMapping
    public ResponseEntity<List<JobPositionResponseDTO>> getAllJobPositions() {
        return ResponseEntity.ok(jobPositionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPositionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPositionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<JobPositionResponseDTO> create(@Valid @RequestBody JobPositionRequestDTO requestDTO) {
        JobPositionResponseDTO created = jobPositionService.create(requestDTO);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPositionResponseDTO> update(@PathVariable Long id,
                                                         @Valid @RequestBody JobPositionRequestDTO requestDTO) {
        JobPositionResponseDTO updated = jobPositionService.update(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobPositionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}