package com.sprintflow.controller;

import com.sprintflow.dto.CreateIssueRequest;
import com.sprintflow.dto.IssueResponse;
import com.sprintflow.dto.UpdateIssueRequest;
import com.sprintflow.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(
            @Valid @RequestBody CreateIssueRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(issueService.createIssue(request));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<IssueResponse>> getAllIssues(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                issueService.getAllIssues(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponse> getIssueById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                issueService.getIssueById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IssueResponse> updateIssue(
            @PathVariable Long id,
            @Valid @RequestBody UpdateIssueRequest request) {

        return ResponseEntity.ok(
                issueService.updateIssue(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IssueResponse> deleteIssue(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                issueService.deleteIssue(id));
    }

    @PutMapping("/{issueId}/assign-sprint/{sprintId}")
    public ResponseEntity<IssueResponse> assignIssueToSprint(
            @PathVariable Long issueId,
            @PathVariable Long sprintId) {

        return ResponseEntity.ok(
                issueService.assignIssueToSprint(issueId, sprintId)
        );
    }

    @DeleteMapping("/{issueId}/remove-sprint")
    public ResponseEntity<IssueResponse> removeIssueFromSprint(
            @PathVariable Long issueId) {

        return ResponseEntity.ok(
                issueService.removeIssueFromSprint(issueId)
        );
    }
}

