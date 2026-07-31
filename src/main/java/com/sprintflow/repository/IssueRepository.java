package com.sprintflow.repository;

import com.sprintflow.entity.Issue;
import com.sprintflow.entity.IssueStatus;
import com.sprintflow.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue,Long> {
    List<Issue> findByProjectId(Long projectId);
    List<Issue> findBySprintId(Long sprintId);


    //Backlogs
    List<Issue> findByProjectIdAndSprintIsNull(Long projectId);

    @Query("""
       SELECT COUNT(i)
       FROM Issue i
       WHERE i.project.organization.id = :organizationId
       """)
    Long countIssuesByOrganization(@Param("organizationId") Long organizationId);

    @Query("""
       SELECT COUNT(i)
       FROM Issue i
       WHERE i.project.organization.id = :organizationId
       AND i.status = :status
       """)
    Long countIssuesByStatus(
            @Param("organizationId") Long organizationId,
            @Param("status") IssueStatus status);

    @Query("""
       SELECT COUNT(i)
       FROM Issue i
       WHERE i.project.organization.id = :organizationId
       AND i.priority = :priority
       """)
    Long countIssuesByPriority(
            @Param("organizationId") Long organizationId,
            @Param("priority") Priority priority);

    List<Issue> findTop5ByProjectOrganizationIdOrderByCreatedAtDesc(Long organizationId);
}
