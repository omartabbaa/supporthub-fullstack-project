package com.SupportHub.demo.dtos.InputDTOs;

public class PermissionInputDTO {
    private Long userId;
    private Long departmentId;
    private Long projectId;
    private Boolean canAnswer;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Boolean getCanAnswer() {
        return canAnswer;
    }

    public void setCanAnswer(Boolean canAnswer) {
        this.canAnswer = canAnswer;
    }
}
