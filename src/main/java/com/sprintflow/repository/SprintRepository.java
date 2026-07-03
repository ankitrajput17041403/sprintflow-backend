package com.sprintflow.repository;

import com.sprintflow.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
}
