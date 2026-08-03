package com.sprintflow.repository;

import com.sprintflow.entity.Issue;
import com.sprintflow.entity.Sprint;
import com.sprintflow.enums.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    List<Sprint> findByProjectId(Long projectId);
    boolean existsByProjectIdAndStatus(Long projectId, SprintStatus status);

    @Query("""
       SELECT COUNT(s)
       FROM Sprint s
       WHERE s.project.organization.id = :organizationId
       AND s.status = :status
       """)
    Long countSprintsByStatus(
            @Param("organizationId") Long organizationId,
            @Param("status") SprintStatus status);

    Optional<Sprint> findByProjectOrganizationIdAndStatus(
            Long organizationId,
            SprintStatus status
    );
}
