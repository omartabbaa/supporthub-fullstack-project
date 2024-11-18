package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.Permissions;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {
    // Custom methods can be added if necessary
}
