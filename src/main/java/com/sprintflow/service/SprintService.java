package com.sprintflow.service;

import com.sprintflow.dto.*;

import java.util.List;


public interface SprintService {


    //CRUD
    SprintResponse createSprint(CreateSprintRequest request);

    List<SprintResponse> getAllSprints(Long projectId);

    SprintResponse getSprintById(Long id);

    SprintResponse updateSprint(Long id, UpdateSprintRequest request);

    SprintResponse deleteSprint(Long id);

    SprintResponse startSprint(Long id);

    SprintResponse completeSprint(Long id);

    void planSprint(Long sprintId, SprintPlanningRequest request);

    SprintBoardResponse getSprintBoard(Long sprintId);
}
