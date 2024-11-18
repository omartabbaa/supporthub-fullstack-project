package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom methods can be added if necessary
}