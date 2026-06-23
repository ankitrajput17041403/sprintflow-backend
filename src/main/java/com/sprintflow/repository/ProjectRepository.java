package com.sprintflow.repository;

import com.sprintflow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOrganizationId(Long organizationId);

    Optional<Project> findByIdAndOrganizationId(Long projectId, Long organizationId);
}
