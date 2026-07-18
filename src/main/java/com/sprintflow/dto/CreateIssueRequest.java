package com.sprintflow.dto;

import com.sprintflow.entity.IssueStatus;
import com.sprintflow.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateIssueRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Status is required")
    private IssueStatus status;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Project Id is required")
    private Long projectId;
}