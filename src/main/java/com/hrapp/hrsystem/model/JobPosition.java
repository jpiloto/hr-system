package com.hrapp.hrsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(
        name = "job_positions",
        indexes = {
                @Index(name = "idx_job_position_title", columnList = "title")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "department")
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
