package com.sprintflow.dto;

import lombok.Data;

import java.util.List;

@Data
public class SprintBoardResponse {

    private List<BoardIssueResponse> todo;

    private List<BoardIssueResponse> inProgress;

    private List<BoardIssueResponse> done;

}