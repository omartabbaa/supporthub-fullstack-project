package com.SupportHub.demo.mappers;

import org.springframework.stereotype.Component;
import com.SupportHub.demo.models.Question;
import com.SupportHub.demo.models.Project;
import com.SupportHub.demo.dtos.OutputDTOs.QuestionOutputDTO;
import com.SupportHub.demo.dtos.InputDTOs.QuestionInputDTO;

@Component
public class QuestionMapper {

    // Convert Question Entity to QuestionOutputDTO
    public QuestionOutputDTO toDto(Question question) {
        QuestionOutputDTO dto = new QuestionOutputDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setQuestionTitle(question.getQuestionTitle());
        dto.setQuestionText(question.getQuestionText());
        dto.setStatus(question.getStatus());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setProjectId(question.getProject().getProjectId()); // Map Project ID
        return dto;
    }
    

    // Convert QuestionInputDTO to Question Entity
    public Question toEntity(QuestionInputDTO questionInputDTO, Project project) {
        Question question = new Question();
        question.setQuestionTitle(questionInputDTO.getQuestionTitle());
        question.setQuestionText(questionInputDTO.getQuestionText());
        question.setProject(project);  // Project entity already retrieved
        return question;
    }
}
