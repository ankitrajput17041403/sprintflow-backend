package com.sprintflow.service;

import com.sprintflow.dto.CreateSprintRequest;
import com.sprintflow.dto.SprintResponse;
import com.sprintflow.dto.UpdateSprintRequest;

import java.util.List;


public interface SprintService {


    //CRUD
    SprintResponse createSprint(CreateSprintRequest request);

    List<SprintResponse> getAllSprints(Long projectId);

    SprintResponse getSprintById(Long id);

    SprintResponse updateSprint(Long id, UpdateSprintRequest request);

    SprintResponse deleteSprint(Long id);
}
