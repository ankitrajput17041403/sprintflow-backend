package com.sprintflow.repository;

import com.sprintflow.entity.Comment;
import com.sprintflow.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByIssueId(Long issueId);
    List<Comment> findByIssueIdOrderByCreatedAtDesc(Long issueId);

}
