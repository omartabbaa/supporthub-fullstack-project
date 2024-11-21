package com.SupportHub.demo.mappers;

import org.springframework.stereotype.Component;

import com.SupportHub.demo.dtos.InputDTOs.QuestionLikesInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.QuestionLikesOutputDTO;
import com.SupportHub.demo.models.Question;
import com.SupportHub.demo.models.QuestionLikes;
import com.SupportHub.demo.models.User;

@Component
public class QuestionLikesMapper {

    // Convert QuestionLikes Entity to QuestionLikesOutputDTO
    public QuestionLikesOutputDTO toDto(QuestionLikes questionLike) {
        QuestionLikesOutputDTO dto = new QuestionLikesOutputDTO();
        dto.setLikeId(questionLike.getLikeId());
        dto.setQuestionId(questionLike.getQuestion().getQuestionId());
        dto.setUserId(questionLike.getUser().getUserId());
        dto.setCreatedAt(questionLike.getCreatedAt());
        return dto;
    }

    // Convert QuestionLikesInputDTO to QuestionLikes Entity
    public QuestionLikes toEntity(QuestionLikesInputDTO questionLikesInputDTO, User user, Question question) {
        QuestionLikes questionLike = new QuestionLikes();
        questionLike.setUser(user);  // User entity already retrieved
        questionLike.setQuestion(question);  // Question entity already retrieved
        return questionLike;
    }
}
