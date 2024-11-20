package com.SupportHub.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.dtos.InputDTOs.ProjectInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.ProjectOutputDTO;
import com.SupportHub.demo.exceptions.ResourceNotFoundException;
import com.SupportHub.demo.mappers.ProjectMapper;
import com.SupportHub.demo.models.Department;
import com.SupportHub.demo.models.Project;
import com.SupportHub.demo.repositories.DepartmentRepository;
import com.SupportHub.demo.repositories.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(
            ProjectRepository projectRepository,
            DepartmentRepository departmentRepository,
            ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.departmentRepository = departmentRepository;
        this.projectMapper = projectMapper;
    }

    // Find a project by its ID
    public Optional<Project> findById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    // Find a project by its ID and map to DTO
    public Optional<ProjectOutputDTO> findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .map(projectMapper::toOutputDTO);
    }

    // Find all projects
    public List<ProjectOutputDTO> findAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    // Create a new project
    public ProjectOutputDTO createProject(ProjectInputDTO projectInputDTO) {
        Department department = getDepartmentFromDTO(projectInputDTO);
        Project project = projectMapper.toEntity(projectInputDTO, department);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toOutputDTO(savedProject);
    }

    // Update an existing project (full update)
    public ProjectOutputDTO updateProject(Long projectId, ProjectInputDTO projectInputDTO) {
        Project project = getProjectById(projectId);
        Department department = getDepartmentFromDTO(projectInputDTO);
        updateProjectFields(project, projectInputDTO, department);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toOutputDTO(updatedProject);
    }

    // Patch an existing project (partial update)
    public ProjectOutputDTO patchProject(Long projectId, ProjectInputDTO projectInputDTO) {
        Project project = getProjectById(projectId);
        updateProjectFieldsIfPresent(project, projectInputDTO);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toOutputDTO(updatedProject);
    }

    // Delete a project by its ID
    public void deleteProjectById(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with ID: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }

    private Department getDepartmentFromDTO(ProjectInputDTO projectInputDTO) {
        if (projectInputDTO.getDepartmentId() == null) {
            return null;
        }
        return departmentRepository.findById(projectInputDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + projectInputDTO.getDepartmentId()));
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found with ID: " + projectId));
    }

    private void updateProjectFields(Project project, ProjectInputDTO projectInputDTO, Department department) {
        project.setName(projectInputDTO.getName());
        project.setDescription(projectInputDTO.getDescription());
        project.setImage(projectInputDTO.getImage());
        project.setAverageResponseTime(projectInputDTO.getAverageResponseTime());
        project.setDepartment(department);
    }

    private void updateProjectFieldsIfPresent(Project project, ProjectInputDTO projectInputDTO) {
        if (projectInputDTO.getName() != null) {
            project.setName(projectInputDTO.getName());
        }
        if (projectInputDTO.getDescription() != null) {
            project.setDescription(projectInputDTO.getDescription());
        }
        if (projectInputDTO.getImage() != null) {
            project.setImage(projectInputDTO.getImage());
        }
        if (projectInputDTO.getAverageResponseTime() != null) {
            project.setAverageResponseTime(projectInputDTO.getAverageResponseTime());
        }
        if (projectInputDTO.getDepartmentId() != null) {
            Department department = getDepartmentFromDTO(projectInputDTO);
            project.setDepartment(department);
        }
    }
}
