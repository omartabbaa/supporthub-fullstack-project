package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // Custom methods can be added if necessary
}
