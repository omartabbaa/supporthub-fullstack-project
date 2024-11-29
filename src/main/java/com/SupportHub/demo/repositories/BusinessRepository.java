package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.Business;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    // Custom methods can be added if necessary
}
