package com.sprintflow.dto;

import com.sprintflow.entity.IssueStatus;
import com.sprintflow.enums.Priority;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentIssueResponse {

    private Long id;

    private String title;

    private IssueStatus status;

    private Priority priority;

    private String projectName;
}