package com.sprintflow.service;

import com.sprintflow.dto.CommentResponse;
import com.sprintflow.dto.CreateCommentRequest;
import com.sprintflow.dto.UpdateCommentRequest;

import java.util.List;

public interface CommentService {


        CommentResponse createComment(Long issueId, CreateCommentRequest request);

        List<CommentResponse> getCommentsByIssue(Long issueId);

        CommentResponse updateComment(Long commentId,
                                      UpdateCommentRequest request);
        CommentResponse deleteComment(Long commentId);
}
