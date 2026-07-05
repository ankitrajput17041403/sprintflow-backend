package com.sprintflow.repository;

import com.sprintflow.entity.Issue;
import com.sprintflow.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    List<Sprint> findByProjectId(Long projectId);

}
