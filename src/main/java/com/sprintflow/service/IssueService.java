package com.sprintflow.service;

import com.sprintflow.dto.*;
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

    //Meove Issue Status
    void updateIssueStatus(Long issueId, UpdateIssueStatusRequest request);

    IssueResponse assignIssueToUser(Long issueId, Long userId);

    //Search
    List<IssueResponse> searchIssues(IssueSearchRequest request);
}
