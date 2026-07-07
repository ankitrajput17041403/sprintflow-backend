package com.sprintflow.controller;


import com.sprintflow.dto.CreateSprintRequest;
import com.sprintflow.dto.SprintResponse;
import com.sprintflow.dto.UpdateSprintRequest;

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

    @PostMapping
    public ResponseEntity<SprintResponse> createSprint(@Valid @RequestBody CreateSprintRequest request){
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
}
