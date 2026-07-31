package com.sprintflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalProjects;

    private Long totalIssues;

    private Long openIssues;

    private Long inProgressIssues;

    private Long doneIssues;

    private Long activeSprints;

    private Long completedSprints;

    private Long lowPriorityIssues;

    private Long mediumPriorityIssues;

    private Long highPriorityIssues;

    private List<RecentIssueResponse> recentIssues;
}