package com.sprintflow.service;

import com.sprintflow.dto.CommentResponse;
import com.sprintflow.dto.CreateCommentRequest;

import java.util.List;

public interface CommentService {


        CommentResponse createComment(Long issueId, CreateCommentRequest request);

        List<CommentResponse> getCommentsByIssue(Long issueId);


}
