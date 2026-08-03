package com.sprintflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SprintProgressResponse {

    private String sprintName;

    private Long totalIssues;

    private Long completedIssues;

    private Long remainingIssues;

    private Integer progressPercentage;
}