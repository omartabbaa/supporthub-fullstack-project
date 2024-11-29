package com.SupportHub.demo.dtos.InputDTOs;

public class QuestionLikesInputDTO {
    private Long questionId;
    private Long userId;

    // Getters and Setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
