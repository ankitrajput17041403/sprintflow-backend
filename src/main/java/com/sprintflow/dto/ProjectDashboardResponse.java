package com.sprintflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDashboardResponse {

    //Project Name
    private String projectName;

    //Issues
    private Long totalIssues;
    private Long todoIssues;
    private Long inProgressIssues;
    private Long doneIssues;

    //Sprint
    private Long totalSprints;
    private Long plannedSprints;
    private Long activeSprints;
    private Long completedSprints;


}