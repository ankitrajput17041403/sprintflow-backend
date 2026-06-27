package com.sprintflow.dto;

import com.sprintflow.entity.IssueStatus;
import com.sprintflow.entity.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIssueRequest {
    private String title;
    private String description;
    private IssueStatus status;
    private Priority priority;
}
