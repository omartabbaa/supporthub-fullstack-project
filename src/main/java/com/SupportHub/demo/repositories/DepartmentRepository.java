package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Custom methods can be added if necessary
}
