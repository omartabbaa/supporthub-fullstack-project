package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
  
}
