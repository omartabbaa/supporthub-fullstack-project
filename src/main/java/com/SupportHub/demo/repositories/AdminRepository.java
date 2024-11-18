package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUser_UserId(Long userId);
}