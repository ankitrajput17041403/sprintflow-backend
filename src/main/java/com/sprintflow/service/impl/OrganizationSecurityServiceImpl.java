package com.sprintflow.service.impl;


import com.sprintflow.entity.Issue;
import com.sprintflow.entity.Project;
import com.sprintflow.entity.Sprint;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.OrganizationSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class OrganizationSecurityServiceImpl implements OrganizationSecurityService {

    private final CurrentUserService currentUserService;
    @Override
    public void validateProjectAccess(Project project) {

        Long currentUserServiceId = currentUserService.getCurrentOrganizationId();

        if(!currentUserServiceId.equals(project.getOrganization().getId())){
            throw new RuntimeException("Access denied");        }
    }

    @Override
    public void validateIssueAccess(Issue issue) {
        Long currentUserServiceId = currentUserService.getCurrentOrganizationId();
        if(!currentUserServiceId.equals(issue.getProject().getOrganization().getId())){
            throw new RuntimeException("Access denied");        }

    }

    @Override
    public void validateSprintAccess(Sprint sprint) {
        Long currentUserServiceId = currentUserService.getCurrentOrganizationId();
        if(!currentUserServiceId.equals(sprint.getProject().getOrganization().getId())){
            throw new RuntimeException("Access denied");        }
    }



    }

