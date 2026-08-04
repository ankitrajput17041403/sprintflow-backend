package com.sprintflow.dto;

import com.sprintflow.entity.IssueStatus;
import com.sprintflow.enums.Priority;
import lombok.Data;

@Data
public class IssueSearchRequest {

    private Long projectId;

    private Long assigneeId;

    private IssueStatus status;

    private Priority priority;
}