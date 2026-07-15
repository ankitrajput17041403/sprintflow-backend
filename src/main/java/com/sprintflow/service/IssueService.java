package com.sprintflow.service;

import com.sprintflow.dto.CreateIssueRequest;
import com.sprintflow.dto.IssueResponse;
import com.sprintflow.dto.UpdateIssueRequest;
import com.sprintflow.entity.Project;

import java.util.List;

public interface IssueService {
    //CRUD
    IssueResponse createIssue(CreateIssueRequest request);

    List<IssueResponse> getAllIssues(Long projectId);

    IssueResponse getIssueById(Long id);

    IssueResponse updateIssue(Long id, UpdateIssueRequest request);

    IssueResponse deleteIssue(Long id);


    //Sprint Operations
    IssueResponse assignIssueToSprint(Long issueId, Long sprintId);

    List<IssueResponse> getIssuesBySprint(Long sprintId);

    IssueResponse removeIssueFromSprint(Long issueId);


    //Backlog
    List<IssueResponse> getBacklogIssues(Long projectId);
}
