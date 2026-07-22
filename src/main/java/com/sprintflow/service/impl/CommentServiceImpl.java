package com.sprintflow.service.impl;

import com.sprintflow.dto.CommentResponse;
import com.sprintflow.dto.CreateCommentRequest;
import com.sprintflow.entity.Comment;
import com.sprintflow.entity.Issue;
import com.sprintflow.entity.Organization;
import com.sprintflow.entity.User;
import com.sprintflow.repository.CommentRepository;
import com.sprintflow.repository.IssueRepository;
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


    @Override
    public CommentResponse createComment(Long issueId, CreateCommentRequest request) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(()->new RuntimeException("Issue Not Found"));

        organizationSecurityService.validateIssueAccess(issue);

        User currentUser =
                currentUserService.getCurrentUser();


        Comment comment = new Comment();
        comment.setMessage(request.getMessage());
        comment.setIssue(issue);
        comment.setCreatedBy(currentUser);
        comment= commentRepository.save(comment);



        return  mapToResponse(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByIssue(Long issueId) {
        return List.of();
    }

    public CommentResponse mapToResponse(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getMessage(),
                comment.getCreatedAt(),
                comment.getCreatedBy().getName()
        );
    }
}
