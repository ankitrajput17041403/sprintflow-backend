package com.sprintflow.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CreateCommentRequest {

        @NotBlank(message = "Comment message is required")
        private String message;
    }

