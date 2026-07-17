package com.sprintflow.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SprintPlanningRequest {

    @NotEmpty(message = "Issue IDs cannot be empty")
    private List<Long> issueIds;
}