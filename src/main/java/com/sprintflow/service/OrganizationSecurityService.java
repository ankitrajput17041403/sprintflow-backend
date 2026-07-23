package com.sprintflow.service;

import com.sprintflow.entity.*;

public interface OrganizationSecurityService {

        void validateProjectAccess(Project project);

        void validateIssueAccess(Issue issue);

        void validateSprintAccess(Sprint sprint);

        void validateIssueAndUser(Issue issue, User user);

        void validateCommentOwnership(Comment comment);
    }
