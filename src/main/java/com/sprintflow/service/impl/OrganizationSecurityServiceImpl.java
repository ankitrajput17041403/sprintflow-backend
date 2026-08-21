package com.sprintflow.service.impl;

import com.sprintflow.entity.*;
import com.sprintflow.enums.Role;
import com.sprintflow.exception.AccessDeniedException;
import com.sprintflow.exception.BusinessException;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.OrganizationSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrganizationSecurityServiceImpl
        implements OrganizationSecurityService {

    private final CurrentUserService currentUserService;

    @Override
    public void validateProjectAccess(Project project) {

        Long currentUserServiceId =
                currentUserService.getCurrentOrganizationId();

        if (!currentUserServiceId.equals(
                project.getOrganization().getId())) {

            throw new AccessDeniedException("Access denied");
        }
    }


    @Override
    public void validateIssueAccess(Issue issue) {

        Long currentUserServiceId =
                currentUserService.getCurrentOrganizationId();

        if (!currentUserServiceId.equals(
                issue.getProject().getOrganization().getId())) {

            throw new AccessDeniedException("Access denied");
        }
    }


    @Override
    public void validateSprintAccess(Sprint sprint) {

        Long currentUserServiceId =
                currentUserService.getCurrentOrganizationId();

        if (!currentUserServiceId.equals(
                sprint.getProject().getOrganization().getId())) {

            throw new AccessDeniedException("Access denied");
        }
    }



    @Override
    public void validateIssueAndUser(Issue issue, User user) {

        if (!issue.getProject().getOrganization().getId()
                .equals(user.getOrganization().getId())) {

            throw new BusinessException(
                    "Issue and User must belong to the same organization");
        }
    }



    @Override
    public void validateCommentOwnership(Comment comment) {

        User currentUser = currentUserService.getCurrentUser();

        if (currentUser.getRole() == Role.ORG_ADMIN) {
            return;
        }

        if (!comment.getCreatedBy().getId()
                .equals(currentUser.getId())) {

            throw new AccessDeniedException(
                    "You can only modify your own comments");
        }
    }


}