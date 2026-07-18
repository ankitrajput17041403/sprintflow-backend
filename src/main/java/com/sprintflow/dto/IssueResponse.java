package com.sprintflow.dto;


import com.sprintflow.entity.IssueStatus;
import com.sprintflow.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponse {

    private Long id;

    private String title;

    private String description;

    private IssueStatus status;

    private Priority priority;

    private LocalDateTime createdAt;

    private String projectName;

    private String sprintName;
}