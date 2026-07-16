package com.sprintflow.controller;

import com.sprintflow.dto.CreateProjectRequest;
import com.sprintflow.dto.IssueResponse;
import com.sprintflow.dto.ProjectResponse;
import com.sprintflow.dto.UpdateProjectRequest;
import com.sprintflow.service.IssueService;
import com.sprintflow.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final IssueService issueService;

    @PostMapping("/create")
    public ProjectResponse createProject(
            @Valid @RequestBody CreateProjectRequest request) {

        return projectService.createProject(request);
    }

    @GetMapping("/all")
    public List<ProjectResponse> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable  Long id){
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(
            @PathVariable Long id,
            @RequestBody UpdateProjectRequest request) {

        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    public ProjectResponse updateProject(
            @PathVariable Long id) {

        return projectService.deleteProject(id);
    }


    //Backlog Strt frm here---

    @GetMapping("/{projectId}/backlog")
    public ResponseEntity<List<IssueResponse>> getBacklogIssues(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                issueService.getBacklogIssues(projectId)
        );
    }
}