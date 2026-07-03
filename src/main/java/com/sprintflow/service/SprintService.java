package com.sprintflow.service;

import com.sprintflow.dto.CreateSprintRequest;
import com.sprintflow.dto.SprintResponse;
import com.sprintflow.dto.UpdateSprintRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SprintService {

    SprintResponse createSprint(CreateSprintRequest request);

    List<SprintResponse> getAllSprints(Long projectId);

    SprintResponse getSprintById(Long id);

    SprintResponse updateSprint(Long id, UpdateSprintRequest request);

    SprintResponse deleteSprint(Long id);
}
