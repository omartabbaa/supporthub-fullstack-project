package com.SupportHub.demo.mappers;

import org.springframework.stereotype.Component;

import com.SupportHub.demo.dtos.InputDTOs.ProjectInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.ProjectOutputDTO;
import com.SupportHub.demo.models.Department;
import com.SupportHub.demo.models.Project;

@Component
public class ProjectMapper {

    // Convert Project entity to ProjectOutputDTO
    public ProjectOutputDTO toOutputDTO(Project project) {
        ProjectOutputDTO dto = new ProjectOutputDTO();
        dto.setProjectId(project.getProjectId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setImage(project.getImage());
        dto.setAverageResponseTime(project.getAverageResponseTime());

        // Handle potential null value for department
        if (project.getDepartment() != null) {
            dto.setDepartmentId(project.getDepartment().getDepartmentId());
        } else {
            dto.setDepartmentId(null);
        }

        return dto;
    }

    // Convert ProjectInputDTO to Project entity
    public Project toEntity(ProjectInputDTO dto, Department department) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setImage(dto.getImage());
        project.setAverageResponseTime(dto.getAverageResponseTime());
        project.setDepartment(department); // Set the department
        return project;
    }
}
