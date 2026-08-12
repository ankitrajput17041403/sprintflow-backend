package com.sprintflow.service.impl;


import com.sprintflow.dto.CommentResponse;
import com.sprintflow.dto.CreateCommentRequest;
import com.sprintflow.dto.UpdateCommentRequest;
import com.sprintflow.entity.*;
import com.sprintflow.enums.ActivityAction;
import com.sprintflow.repository.CommentRepository;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.service.ActivityService;
import com.sprintflow.service.CommentService;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.OrganizationSecurityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final IssueRepository issueRepository;
    private final OrganizationSecurityService organizationSecurityService;
    private final CurrentUserService currentUserService;
    private final CommentRepository commentRepository;
    private final ActivityService activityService;


    @Override
    public CommentResponse createComment(Long issueId, CreateCommentRequest request) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue Not Found"));

        organizationSecurityService.validateIssueAccess(issue);

        User currentUser = currentUserService.getCurrentUser();

        Comment comment = new Comment();
        comment.setMessage(request.getMessage());
        comment.setIssue(issue);
        comment.setCreatedBy(currentUser);
        comment = commentRepository.save(comment);

        activityService.logActivity(
                issue,
                currentUser,
                ActivityAction.COMMENT_ADDED,
                "Comment added"
        );

        return mapToResponse(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByIssue(Long issueId) {

        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue Not Found"));

        organizationSecurityService.validateIssueAccess(issue);

        List<Comment> comments = commentRepository.findByIssueIdOrderByCreatedAtDesc(issueId);
        return comments.stream().map(comment -> mapToResponse(comment)).toList();
    }

    @Override
    public CommentResponse updateComment(Long commentId,
                                         UpdateCommentRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new RuntimeException("Comment Not Found"));

        organizationSecurityService
                .validateIssueAccess(comment.getIssue());

        organizationSecurityService.validateCommentOwnership(comment);

        comment.setMessage(request.getMessage());

        comment = commentRepository.save(comment);

        activityService.logActivity(
                comment.getIssue(),
                currentUserService.getCurrentUser(),
                ActivityAction.COMMENT_UPDATED,
                "Comment updated"
        );

        return mapToResponse(comment);

    }

    @Override
    public CommentResponse deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new RuntimeException("Comment Not Found"));

        organizationSecurityService
                .validateIssueAccess(comment.getIssue());

        organizationSecurityService
                .validateCommentOwnership(comment);

        activityService.logActivity(
                comment.getIssue(),
                currentUserService.getCurrentUser(),
                ActivityAction.COMMENT_DELETED,
                "Comment deleted"
        );

        commentRepository.delete(comment);

        return mapToResponse(comment);
    }


    public CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getMessage(), comment.getCreatedAt(), comment.getCreatedBy().getName());
    }
}



