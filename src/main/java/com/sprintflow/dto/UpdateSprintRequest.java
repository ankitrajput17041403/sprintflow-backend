package com.sprintflow.dto;

import com.sprintflow.enums.SprintStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateSprintRequest {


        @NotBlank
        private String name;

        @NotBlank
        private String goal;

        @NotNull
        private LocalDate startDate;

        @NotNull
        private LocalDate endDate;

        @NotNull
        private SprintStatus status;
    }


