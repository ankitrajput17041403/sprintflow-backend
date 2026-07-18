package com.sprintflow.dto;

import com.sprintflow.entity.IssueStatus;
import com.sprintflow.enums.Priority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardIssueResponse {

    private Long id;

    private String title;

    private Priority priority;

    private IssueStatus issueStatus;

}