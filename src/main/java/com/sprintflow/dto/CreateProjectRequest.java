package com.sprintflow.dto;

import com.sprintflow.entity.Organization;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectRequest {

    @NotBlank
    private String name;

    private String description;

    private Organization organizationId;
}