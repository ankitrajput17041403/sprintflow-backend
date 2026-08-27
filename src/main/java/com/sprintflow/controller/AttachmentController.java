package com.sprintflow.controller;


import com.sprintflow.dto.AttachmentResponse;
import com.sprintflow.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    @PostMapping("/issues/{issueId}")
    public ResponseEntity<AttachmentResponse> uploadAttachment(
            @PathVariable Long issueId,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attachmentService.uploadAttachment(issueId, file));
    }

    @GetMapping("/issues/{issueId}")
    public ResponseEntity<List<AttachmentResponse>> getAttachmentsByIssue(
            @PathVariable Long issueId) {

        return ResponseEntity.ok(
                attachmentService.getAttachmentsByIssue(issueId));
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<AttachmentResponse> deleteAttachment(
            @PathVariable Long attachmentId) {

        return ResponseEntity.ok(
                attachmentService.deleteAttachment(attachmentId)
        );
    }
    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long attachmentId) {


        Resource resource =
                attachmentService.downloadAttachment(attachmentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}