package com.SupportHub.demo.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.Business;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByName(String name);
}
