package com.SupportHub.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SupportHub.demo.dtos.InputDTOs.ProjectInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.ProjectOutputDTO;
import com.SupportHub.demo.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // GET /api/projects/{projectId}
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectOutputDTO> getProjectById(@PathVariable Long projectId) {
        Optional<ProjectOutputDTO> project = projectService.findProjectById(projectId);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /api/projects
    @GetMapping
    public ResponseEntity<List<ProjectOutputDTO>> getAllProjects() {
        List<ProjectOutputDTO> projects = projectService.findAllProjects();
        return ResponseEntity.ok(projects);
    }

    // POST /api/projects
    @PostMapping
    public ResponseEntity<ProjectOutputDTO> createProject(@RequestBody ProjectInputDTO projectInputDTO) {
        ProjectOutputDTO createdProject = projectService.createProject(projectInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    // PUT /api/projects/{projectId}
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectOutputDTO> updateProject(@PathVariable Long projectId,
                                                          @RequestBody ProjectInputDTO projectInputDTO) {
        try {
            ProjectOutputDTO updatedProject = projectService.updateProject(projectId, projectInputDTO);
            return ResponseEntity.ok(updatedProject);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE /api/projects/{projectId}
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        try {
            projectService.deleteProjectById(projectId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<ProjectOutputDTO> patchProject(@PathVariable Long projectId,
                                                        @RequestBody ProjectInputDTO projectInputDTO) {
        ProjectOutputDTO updatedProject = projectService.patchProject(projectId, projectInputDTO);
        return ResponseEntity.ok(updatedProject);
    }

    
}
