package com.sprintflow.service;

import com.sprintflow.entity.Issue;
import com.sprintflow.entity.Project;
import com.sprintflow.entity.Sprint;

public interface OrganizationSecurityService {

        void validateProjectAccess(Project project);

        void validateIssueAccess(Issue issue);

        void validateSprintAccess(Sprint sprint);


    }
