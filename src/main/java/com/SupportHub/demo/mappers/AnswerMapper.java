package com.SupportHub.demo.mappers;

import org.springframework.stereotype.Component;
import com.SupportHub.demo.models.Answer;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.models.Question;
import com.SupportHub.demo.dtos.OutputDTOs.AnswerOutputDTO;
import com.SupportHub.demo.dtos.InputDTOs.AnswerInputDTO;

@Component
public class AnswerMapper {

    // Convert Answer Entity to AnswerOutputDTO
    public AnswerOutputDTO toDto(Answer answer) {
        AnswerOutputDTO dto = new AnswerOutputDTO();
        dto.setAnswerId(answer.getAnswerId());
        dto.setAnswerText(answer.getAnswerText());
        dto.setQuestionId(answer.getQuestion().getQuestionId());
        dto.setUserId(answer.getUser().getUserId());
        dto.setCreatedAt(answer.getCreatedAt());
        return dto;
    }

    // Convert AnswerInputDTO to Answer Entity
    public Answer toEntity(AnswerInputDTO answerInputDTO, User user, Question question) {
        Answer answer = new Answer();
        answer.setAnswerText(answerInputDTO.getAnswerText());
        answer.setUser(user);  // User entity already retrieved
        answer.setQuestion(question);  // Question entity already retrieved
        return answer;
    }
}
