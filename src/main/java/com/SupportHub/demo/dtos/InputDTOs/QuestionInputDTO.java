package com.SupportHub.demo.dtos.InputDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class QuestionInputDTO {

    @NotBlank(message = "Question title cannot be blank")
    
    private String questionTitle;

    @NotBlank(message = "Question text cannot be blank")
   
    private String questionText;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    // Getters and Setters
    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
