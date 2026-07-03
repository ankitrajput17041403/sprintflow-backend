package com.sprintflow.service.impl;

import com.sprintflow.dto.CreateSprintRequest;
import com.sprintflow.dto.SprintResponse;
import com.sprintflow.dto.UpdateSprintRequest;
import com.sprintflow.service.SprintService;

import java.util.List;

public class SprintServiceImpl implements SprintService {
    @Override
    public SprintResponse createSprint(CreateSprintRequest request) {
        return null;
    }

    @Override
    public List<SprintResponse> getAllSprints(Long projectId) {
        return List.of();
    }

    @Override
    public SprintResponse getSprintById(Long id) {
        return null;
    }

    @Override
    public SprintResponse updateSprint(Long id, UpdateSprintRequest request) {
        return null;
    }

    @Override
    public SprintResponse deleteSprint(Long id) {
        return null;
    }
}
