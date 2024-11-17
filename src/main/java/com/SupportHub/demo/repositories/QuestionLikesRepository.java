package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SupportHub.demo.models.QuestionLikes;


public interface QuestionLikesRepository extends JpaRepository<QuestionLikes, Long> {
    
}
