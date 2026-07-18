package com.sprintflow.controller;


import com.sprintflow.dto.*;

import com.sprintflow.service.IssueService;
import com.sprintflow.service.SprintService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RequestMapping("/api/sprints")
@RestController
public class SprintController {

    private final SprintService sprintService;
    private final IssueService issueService;

    @PostMapping
    public ResponseEntity<SprintResponse> createSprint(
            @Valid @RequestBody CreateSprintRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(sprintService.createSprint(request));

    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<SprintResponse>> getAllSprints(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                sprintService.getAllSprints(projectId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintResponse> getSprintById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                sprintService.getSprintById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintResponse> updateSprint(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSprintRequest request) {

        return ResponseEntity.ok(
                sprintService.updateSprint(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SprintResponse> deleteSprint(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                sprintService.deleteSprint(id)
        );
    }
    @GetMapping("/{sprintId}/issues")
    public ResponseEntity<List<IssueResponse>> getIssuesBySprint(
            @PathVariable Long sprintId) {

        return ResponseEntity.ok(
                issueService.getIssuesBySprint(sprintId)
        );
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<SprintResponse> startSprint(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                sprintService.startSprint(id)
        );
    }
    @PutMapping("/{id}/complete")
    public ResponseEntity<SprintResponse> completeSprint(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                sprintService.completeSprint(id)
        );
    }
    @PutMapping("/{sprintId}/plan")
    public ResponseEntity<Void> planSprint(
            @PathVariable Long sprintId,
            @Valid @RequestBody SprintPlanningRequest request) {

        sprintService.planSprint(sprintId, request);

        return ResponseEntity.noContent().build();
    }

    //Board
    @GetMapping("/{sprintId}/board")
    public ResponseEntity<SprintBoardResponse> getSprintBoard(@PathVariable Long sprintId) {

        return ResponseEntity.ok(sprintService.getSprintBoard(sprintId));
    }
}
