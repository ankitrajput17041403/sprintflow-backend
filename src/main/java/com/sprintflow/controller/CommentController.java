package com.sprintflow.controller;

import com.sprintflow.dto.CommentResponse;
import com.sprintflow.dto.CreateCommentRequest;
import com.sprintflow.dto.UpdateCommentRequest;
import com.sprintflow.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/issue/{issueId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long issueId,
            @Valid @RequestBody CreateCommentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(issueId, request));
    }


    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByIssue(
            @PathVariable Long issueId) {

        return ResponseEntity.ok(
                commentService.getCommentsByIssue(issueId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request) {

        return ResponseEntity.ok(
                commentService.updateComment(commentId, request)
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(
            @PathVariable Long commentId) {

        return ResponseEntity.ok(
                commentService.deleteComment(commentId));
    }
}