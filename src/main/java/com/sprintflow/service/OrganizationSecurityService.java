package com.sprintflow.service;

import com.sprintflow.entity.Issue;
import com.sprintflow.entity.Project;
import com.sprintflow.entity.Sprint;
import com.sprintflow.entity.User;

public interface OrganizationSecurityService {

        void validateProjectAccess(Project project);

        void validateIssueAccess(Issue issue);

        void validateSprintAccess(Sprint sprint);

        void validateIssueAndUser(Issue issue, User user);
    }
