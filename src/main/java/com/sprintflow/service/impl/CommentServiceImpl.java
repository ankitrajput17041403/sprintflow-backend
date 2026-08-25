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


        System.out.println("===== COMMENT CREATED =====");

        System.out.println(
                "Issue Creator ID: "
                        + issue.getCreatedBy().getId());

        System.out.println(
                "Current User ID: "
                        + currentUser.getId());


        if (!issue.getCreatedBy().getId()
                .equals(currentUser.getId())) {

            System.out.println(
                    "===== CREATING COMMENT NOTIFICATION =====");

            notificationService.createNotification(
                    issue.getCreatedBy(),
                    "New comment on issue: "
                            + issue.getTitle()
            );
        }


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