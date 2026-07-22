package com.sprintflow.service.impl;

import com.sprintflow.dto.CommentResponse;
import com.sprintflow.dto.CreateCommentRequest;
import com.sprintflow.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public CommentResponse createComment(Long issueId, CreateCommentRequest request) {
        return null;
    }

    @Override
    public List<CommentResponse> getCommentsByIssue(Long issueId) {
        return List.of();
    }
}
