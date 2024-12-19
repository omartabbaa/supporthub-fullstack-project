package com.SupportHub.demo.dtos.InputDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProjectInputDTO {

    @NotBlank(message = "Project name cannot be blank")

    private String name;

  
    private String description;

    private String image;

    private Double averageResponseTime;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(Double averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
