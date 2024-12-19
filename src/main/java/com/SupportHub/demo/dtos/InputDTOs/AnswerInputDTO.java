package com.SupportHub.demo.dtos.InputDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnswerInputDTO {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Answer text cannot be blank")
    private String answerText;

    // Getters and Setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
