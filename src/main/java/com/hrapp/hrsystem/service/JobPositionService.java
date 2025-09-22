package com.hrapp.hrsystem.service;

import com.hrapp.hrsystem.dto.JobPositionRequestDTO;
import com.hrapp.hrsystem.dto.JobPositionResponseDTO;
import com.hrapp.hrsystem.exception.ResourceNotFoundException;
import com.hrapp.hrsystem.mapper.JobPositionMapper;
import com.hrapp.hrsystem.model.JobPosition;
import com.hrapp.hrsystem.repository.JobPositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPositionService {

    private final JobPositionRepository jobPositionRepository;
    private final JobPositionMapper jobPositionMapper;

    public JobPositionService(JobPositionRepository jobPositionRepository, JobPositionMapper jobPositionMapper) {
        this.jobPositionRepository = jobPositionRepository;
        this.jobPositionMapper = jobPositionMapper;
    }

    public List<JobPositionResponseDTO> getAll() {
        return jobPositionRepository.findAll().stream()
                .map(jobPositionMapper::toResponseDTO)
                .toList();
    }

    public JobPositionResponseDTO getById(Long id) {
        JobPosition jobPosition = jobPositionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosition not found with id " + id));
        return jobPositionMapper.toResponseDTO(jobPosition);
    }

    public JobPositionResponseDTO create(JobPositionRequestDTO requestDTO) {
        JobPosition jobPosition = jobPositionMapper.toEntity(requestDTO);
        JobPosition saved = jobPositionRepository.save(jobPosition);
        return jobPositionMapper.toResponseDTO(saved);
    }

    public JobPositionResponseDTO update(Long id, JobPositionRequestDTO requestDTO) {
        JobPosition existing = jobPositionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosition not found with id " + id));

        existing.setTitle(requestDTO.getTitle());
        existing.setDescription(requestDTO.getDescription());

        JobPosition saved = jobPositionRepository.save(existing);
        return jobPositionMapper.toResponseDTO(saved);
    }

    public void delete(Long id) {
        JobPosition existing = jobPositionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosition not found with id " + id));
        jobPositionRepository.delete(existing);
    }
}