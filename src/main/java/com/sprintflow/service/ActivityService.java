package com.sprintflow.service;


import com.sprintflow.dto.ActivityResponse;

import com.sprintflow.entity.Issue;
import com.sprintflow.entity.User;
import com.sprintflow.enums.ActivityAction;

import java.util.List;

public interface ActivityService {

    void logActivity(
            Issue issue,
            User performedBy,
            ActivityAction action,
            String description
    );

    List<ActivityResponse> getActivitiesByIssue(Long issueId);
}
