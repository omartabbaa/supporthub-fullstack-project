package com.SupportHub.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SupportHub.demo.models.Question;
import com.SupportHub.demo.models.QuestionLikes;
import com.SupportHub.demo.models.User;

public interface QuestionLikesRepository extends JpaRepository<QuestionLikes, Long> {
    QuestionLikes findByQuestionAndUser(Question question, User user);
}
