package com.sprintflow.repository;

import com.sprintflow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOrganizationId(Long organizationId);

    Optional<Project> findByIdAndOrganizationId(
            Long projectId,
            Long organizationId
    );

    @Query("""
            SELECT COUNT(p)
            FROM Project p
            WHERE p.organization.id = :organizationId
            """)
    Long countProjectsByOrganization(
            @Param("organizationId") Long organizationId
    );
}