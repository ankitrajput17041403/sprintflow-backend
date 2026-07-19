package com.sprintflow.dto;

import com.sprintflow.entity.IssueStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateIssueStatusRequest {

    @NotNull(message = "Status is required")
    private IssueStatus issueStatus;
}