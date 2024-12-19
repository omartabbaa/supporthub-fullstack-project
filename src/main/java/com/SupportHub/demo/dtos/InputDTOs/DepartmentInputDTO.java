package com.SupportHub.demo.dtos.InputDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class DepartmentInputDTO {

    @NotBlank(message = "Department name cannot be blank")
 
    private String departmentName;

    
    private String description;

    @NotNull(message = "Business ID is required")
    private Long businessId;

    // Getters and Setters
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}