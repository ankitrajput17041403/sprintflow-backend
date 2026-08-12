package com.sprintflow.repository;

import com.sprintflow.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByIssueIdOrderByCreatedAtDesc(Long issueId);


}
