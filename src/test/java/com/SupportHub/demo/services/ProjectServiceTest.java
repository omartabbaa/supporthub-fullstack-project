package com.SupportHub.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.SupportHub.demo.dtos.InputDTOs.ProjectInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.ProjectOutputDTO;
import com.SupportHub.demo.exceptions.ResourceNotFoundException;
import com.SupportHub.demo.mappers.ProjectMapper;
import com.SupportHub.demo.models.Department;
import com.SupportHub.demo.models.Project;
import com.SupportHub.demo.repositories.DepartmentRepository;
import com.SupportHub.demo.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectInputDTO inputDTO;
    private ProjectOutputDTO outputDTO;
    private Department department;

    @BeforeEach
    void setUp() {
        project = new Project();
        inputDTO = new ProjectInputDTO();
        outputDTO = new ProjectOutputDTO();
        department = new Department();
        
        // Reset all mocks before each test
        reset(projectRepository, departmentRepository, projectMapper);
    }

    @Test
    void testFindProjectByIdSuccess() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        ProjectOutputDTO projectDTO = new ProjectOutputDTO();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectMapper.toOutputDTO(project)).thenReturn(projectDTO);

        // Act
        Optional<ProjectOutputDTO> result = projectService.findProjectById(projectId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(projectDTO, result.get());
    }

    @Test
    void testFindProjectByIdNotFound() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act
        Optional<ProjectOutputDTO> result = projectService.findProjectById(projectId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateProject() {
        // Arrange
        ProjectInputDTO inputDTO = new ProjectInputDTO();
        inputDTO.setName("New Project");
        inputDTO.setDescription("Project Description");
        inputDTO.setDepartmentId(1L);

        Department department = new Department();
        Project project = new Project();
        Project savedProject = new Project();
        ProjectOutputDTO outputDTO = new ProjectOutputDTO();

        when(departmentRepository.findById(inputDTO.getDepartmentId())).thenReturn(Optional.of(department));
        when(projectMapper.toEntity(inputDTO, department)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(savedProject);
        when(projectMapper.toOutputDTO(savedProject)).thenReturn(outputDTO);

        // Act
        ProjectOutputDTO result = projectService.createProject(inputDTO);

        // Assert
        assertEquals(outputDTO, result);
        verify(departmentRepository).findById(1L);
        verify(projectMapper).toEntity(inputDTO, department);
        verify(projectRepository).save(project);
        verify(projectMapper).toOutputDTO(savedProject);
    }

    @Test
    void testDeleteProjectById() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.existsById(projectId)).thenReturn(true);

        // Act
        projectService.deleteProjectById(projectId);

        // Assert
        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @Test
    void testDeleteProjectByIdNotFound() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.existsById(projectId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> projectService.deleteProjectById(projectId));
    }

    @Test
    void testFindAllProjects() {
        // Arrange
        Project project = new Project();
        ProjectOutputDTO projectDTO = new ProjectOutputDTO();
        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(projectMapper.toOutputDTO(project)).thenReturn(projectDTO);

        // Act
        List<ProjectOutputDTO> result = projectService.findAllProjects();

        // Assert
        assertEquals(1, result.size());
        assertEquals(projectDTO, result.get(0));
    }

    @Test
    void testUpdateProjectSuccess() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        Department department = new Department();
        ProjectInputDTO inputDTO = new ProjectInputDTO();
        ProjectOutputDTO outputDTO = new ProjectOutputDTO();

        // Set test values in inputDTO
        inputDTO.setName("Test Project");
        inputDTO.setDescription("Test Description");
        inputDTO.setDepartmentId(1L);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(departmentRepository.findById(inputDTO.getDepartmentId())).thenReturn(Optional.of(department));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toOutputDTO(project)).thenReturn(outputDTO);

        // Act
        ProjectOutputDTO result = projectService.updateProject(projectId, inputDTO);

        // Assert
        assertEquals(outputDTO, result);
        verify(projectRepository).save(project);
        
        // Verify project properties were updated
        assertEquals(inputDTO.getName(), project.getName());
        assertEquals(inputDTO.getDescription(), project.getDescription());
        assertEquals(department, project.getDepartment());
    }

    @Test
    void testUpdateProjectNotFound() {
        // Arrange
        Long projectId = 1L;
        ProjectInputDTO inputDTO = new ProjectInputDTO();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> projectService.updateProject(projectId, inputDTO));
    }

    @Test
    void testPatchProjectSuccess() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        ProjectInputDTO inputDTO = new ProjectInputDTO();
        ProjectOutputDTO outputDTO = new ProjectOutputDTO();

        inputDTO.setName("Updated Name");
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toOutputDTO(project)).thenReturn(outputDTO);

        // Act
        ProjectOutputDTO result = projectService.patchProject(projectId, inputDTO);

        // Assert
        assertEquals(outputDTO, result);
        assertEquals("Updated Name", project.getName());
    }

    @Test
    void testPatchProjectNotFound() {
        // Arrange
        Long projectId = 1L;
        ProjectInputDTO inputDTO = new ProjectInputDTO();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> projectService.patchProject(projectId, inputDTO));
    }
}
