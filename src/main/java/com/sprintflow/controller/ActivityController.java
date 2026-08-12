package com.sprintflow.controller;

import com.sprintflow.dto.ActivityResponse;
import com.sprintflow.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<ActivityResponse>> getActivitiesByIssue(
            @PathVariable Long issueId) {

        return ResponseEntity.ok(
                activityService.getActivitiesByIssue(issueId)
        );
    }
}


