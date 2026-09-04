package com.sprintflow.service.impl;

import com.sprintflow.dto.CommentResponse;
import com.sprintflow.dto.CreateCommentRequest;
import com.sprintflow.dto.UpdateCommentRequest;
import com.sprintflow.entity.*;
import com.sprintflow.enums.ActivityAction;
import com.sprintflow.exception.ResourceNotFoundException;
import com.sprintflow.repository.CommentRepository;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final IssueRepository issueRepository;
    private final OrganizationSecurityService organizationSecurityService;
    private final CurrentUserService currentUserService;
    private final CommentRepository commentRepository;
    private final ActivityService activityService;
    private final NotificationService notificationService;


    @Override
    public CommentResponse createComment(
            Long issueId,
            CreateCommentRequest request) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Issue not found"));

        organizationSecurityService.validateIssueAccess(issue);

        User currentUser =
                currentUserService.getCurrentUser();

        Comment comment = new Comment();

        comment.setMessage(request.getMessage());
        comment.setIssue(issue);
        comment.setCreatedBy(currentUser);

        comment = commentRepository.save(comment);

        // Log comment creation for debugging purposes
        log.debug(
                "Comment {} created on issue {} by user {}",
                comment.getId(),
                issue.getId(),
                currentUser.getId()
        );

        // Notify the user assigned to the issue about the new comment
        if (issue.getAssignedTo() != null &&
                !issue.getAssignedTo().getId().equals(currentUser.getId())) {

            notificationService.createNotification(
                    issue.getAssignedTo(),
                    "New comment on issue: " + issue.getTitle()
            );
        }

        // Record the comment action in the issue activity history
        activityService.logActivity(
                issue,
                currentUser,
                ActivityAction.COMMENT_ADDED,
                "Comment added"
        );

        return mapToResponse(comment);
    }


    @Override
    public List<CommentResponse> getCommentsByIssue(
            Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Issue not found"));

        organizationSecurityService.validateIssueAccess(issue);

        List<Comment> comments =
                commentRepository
                        .findByIssueIdOrderByCreatedAtDesc(
                                issueId);

        return comments.stream()
                .map(comment -> mapToResponse(comment))
                .toList();
    }


    @Override
    public CommentResponse updateComment(
            Long commentId,
            UpdateCommentRequest request) {

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Comment not found"));

        organizationSecurityService
                .validateIssueAccess(comment.getIssue());

        organizationSecurityService
                .validateCommentOwnership(comment);

        comment.setMessage(request.getMessage());

        comment = commentRepository.save(comment);

        // Record the comment update in the issue activity history
        activityService.logActivity(
                comment.getIssue(),
                currentUserService.getCurrentUser(),
                ActivityAction.COMMENT_UPDATED,
                "Comment updated"
        );

        return mapToResponse(comment);
    }


    @Override
    public CommentResponse deleteComment(
            Long commentId) {

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Comment not found"));

        organizationSecurityService
                .validateIssueAccess(comment.getIssue());

        organizationSecurityService
                .validateCommentOwnership(comment);

        // Record the comment deletion in the issue activity history
        activityService.logActivity(
                comment.getIssue(),
                currentUserService.getCurrentUser(),
                ActivityAction.COMMENT_DELETED,
                "Comment deleted"
        );

        commentRepository.delete(comment);

        return mapToResponse(comment);
    }


    public CommentResponse mapToResponse(
            Comment comment) {

        return new CommentResponse(
                comment.getId(),
                comment.getMessage(),
                comment.getCreatedAt(),
                comment.getCreatedBy().getName()
        );
    }
}