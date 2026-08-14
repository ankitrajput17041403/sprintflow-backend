package com.sprintflow.repository;

import com.sprintflow.entity.Issue;
import com.sprintflow.entity.IssueStatus;
import com.sprintflow.enums.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
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

    Long countBySprintId(Long sprintId);

    Long countBySprintIdAndStatus(Long sprintId, IssueStatus status);

    //Search
    @Query("""
            SELECT i
            FROM Issue i
            WHERE i.project.organization.id = :organizationId
            AND (:status IS NULL OR i.status = :status)
            """)
    List<Issue> searchIssues(
            @Param("organizationId") Long organizationId,
            @Param("status") IssueStatus status
    );

    @Query("""
            SELECT i
            FROM Issue i
            WHERE i.project.organization.id = :organizationId
            AND (:status IS NULL OR i.status = :status)
            AND (:priority IS NULL OR i.priority = :priority)
            """)
    List<Issue> searchIssues(
            @Param("organizationId") Long organizationId,
            @Param("status") IssueStatus status,
            @Param("priority") Priority priority
    );

    @Query("""
            SELECT i
            FROM Issue i
            WHERE i.project.organization.id = :organizationId
            AND (:status IS NULL OR i.status = :status)
            AND (:priority IS NULL OR i.priority = :priority)
            AND (:projectId IS NULL OR i.project.id = :projectId)
            """)
    List<Issue> searchIssues(
            @Param("organizationId") Long organizationId,
            @Param("status") IssueStatus status,
            @Param("priority") Priority priority,
            @Param("projectId") Long projectId
    );

//    @Query("""
//            SELECT i
//            FROM Issue i
//            WHERE i.project.organization.id = :organizationId
//            AND (:status IS NULL OR i.status = :status)
//            AND (:priority IS NULL OR i.priority = :priority)
//            AND (:projectId IS NULL OR i.project.id = :projectId)
//            AND (:assigneeId IS NULL OR i.assignedTo.id = :assigneeId)
//            """)
//    List<Issue> searchIssues(
//            @Param("organizationId") Long organizationId,
//            @Param("status") IssueStatus status,
//            @Param("priority") Priority priority,
//            @Param("projectId") Long projectId,
//            @Param("assigneeId") Long assigneeId,
//
//    );


    @Query("""
        SELECT i
        FROM Issue i
        WHERE i.project.organization.id = :organizationId
        AND (:status IS NULL OR i.status = :status)
        AND (:priority IS NULL OR i.priority = :priority)
        AND (:projectId IS NULL OR i.project.id = :projectId)
        AND (:assigneeId IS NULL OR i.assignedTo.id = :assigneeId)
        """)
    Page<Issue> searchIssues(
            @Param("organizationId") Long organizationId,
            @Param("status") IssueStatus status,
            @Param("priority") Priority priority,
            @Param("projectId") Long projectId,
            @Param("assigneeId") Long assigneeId,
            Pageable pageable
    );


    Long countByProjectId(Long projectId);

    Long countByProjectIdAndStatus(
            Long projectId,
            IssueStatus status);
}

