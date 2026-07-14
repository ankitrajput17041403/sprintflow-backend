package com.sprintflow.repository;

import com.sprintflow.entity.Issue;
import com.sprintflow.entity.Sprint;
import com.sprintflow.enums.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    List<Sprint> findByProjectId(Long projectId);
    boolean existsByProjectIdAndStatus(Long projectId, SprintStatus status);

}
