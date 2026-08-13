package com.sprintflow.controller;

import com.sprintflow.dto.*;
import com.sprintflow.entity.IssueStatus;
import com.sprintflow.enums.Priority;
import com.sprintflow.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<List<IssueResponse>> getIssuesBySprint(
            @PathVariable Long sprintId) {

        return ResponseEntity.ok(
                issueService.getIssuesBySprint(sprintId)
        );
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

    //Move Status-- means change the status...
    @PutMapping("/{issueId}/status")
    public ResponseEntity<Void> updateIssueStatus(
            @PathVariable Long issueId,
            @Valid @RequestBody UpdateIssueStatusRequest request) {

        issueService.updateIssueStatus(issueId, request);

        return ResponseEntity.noContent().build();
    }


    //Issue and Assigned
    @PreAuthorize("hasAnyRole('ORG_ADMIN','PROJECT_MANAGER')")
    @PutMapping("/{issueId}/assign/{userId}")
    public ResponseEntity<IssueResponse> assignIssueToUser(
            @PathVariable Long issueId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                issueService.assignIssueToUser(issueId, userId)
        );
    }


    @GetMapping("/project/{projectId}/backlog")
    public ResponseEntity<List<IssueResponse>> getBacklogIssues(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                issueService.getBacklogIssues(projectId)
        );
    }

//    //Search
//    @GetMapping ("/search")
//    public ResponseEntity<List<IssueResponse>> searchIssues(
//            @RequestBody IssueSearchRequest request) {
//
//        return ResponseEntity.ok(
//                issueService.searchIssues(request)
//        );
//    }
//
//      @GetMapping("/search")
//      public ResponseEntity<List<IssueResponse>> searchIssues(
//              @RequestParam(required = false) IssueStatus status,
//              @RequestParam(required = false) Priority priority,
//              @RequestParam(required = false) Long projectId,
//              @RequestParam(required = false) Long assigneeId) {
//
//         IssueSearchRequest request = new IssueSearchRequest();
//         request.setStatus(status);
//         request.setPriority(priority);
//         request.setProjectId(projectId);
//         request.setAssigneeId(assigneeId);
//
//         return ResponseEntity.ok(issueService.searchIssues(request));
//      }


    @GetMapping("/search")
    public ResponseEntity<Page<IssueResponse>> searchIssues(

            @RequestParam(required = false) IssueStatus status,

            @RequestParam(required = false) Priority priority,

            @RequestParam(required = false) Long projectId,

            @RequestParam(required = false) Long assigneeId,

            Pageable pageable
    ) {

        IssueSearchRequest request = new IssueSearchRequest();
        request.setStatus(status);
        request.setPriority(priority);
        request.setProjectId(projectId);
        request.setAssigneeId(assigneeId);

        return ResponseEntity.ok(
                issueService.searchIssues(request, pageable)
        );
    }
}

