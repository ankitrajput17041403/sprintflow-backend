package com.sprintflow.dto;

import com.sprintflow.enums.ActivityAction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityResponse {

    private Long id;
    private ActivityAction action;
    private String description;

    private String performedBy;
    private String performedByEmail;

    private LocalDateTime createdAt;
}
