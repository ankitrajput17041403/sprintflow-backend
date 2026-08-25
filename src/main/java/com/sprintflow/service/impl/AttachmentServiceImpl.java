package com.sprintflow.service.impl;

import com.sprintflow.dto.AttachmentResponse;
import com.sprintflow.entity.Attachment;
import com.sprintflow.entity.Issue;
import com.sprintflow.entity.User;
import com.sprintflow.exception.BusinessException;
import com.sprintflow.exception.ResourceNotFoundException;
import com.sprintflow.repository.AttachmentRepository;
import com.sprintflow.repository.IssueRepository;
import com.sprintflow.service.AttachmentService;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.OrganizationSecurityService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final IssueRepository issueRepository;
    private final OrganizationSecurityService organizationSecurityService;
    private final CurrentUserService currentUserService;
    private final AttachmentRepository attachmentRepository;


    @Override
    public AttachmentResponse uploadAttachment(
            Long issueId,
            MultipartFile file) {

        // Find Issue
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Issue not found"));

        // Validate Organization
        organizationSecurityService
                .validateIssueAccess(issue);

        // Get Current User
        User currentUser =
                currentUserService.getCurrentUser();

        // Validate Empty File
        if (file.isEmpty()) {

            throw new BusinessException(
                    "Please select a file to upload.");
        }

        // Validate File Size (10 MB)
        long maxSize = 10 * 1024 * 1024;

        if (file.getSize() > maxSize) {

            throw new BusinessException(
                    "File size must not exceed 10 MB.");
        }

        // Validate File Type
        Set<String> allowedTypes = Set.of(
                "application/pdf",
                "image/png",
                "image/jpeg"
        );

        if (!allowedTypes.contains(
                file.getContentType())) {

            throw new BusinessException(
                    "Only PDF, PNG and JPEG files are allowed.");
        }


        try {

            // Original file name
            String originalFileName =
                    file.getOriginalFilename();

            // Unique file name
            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + originalFileName;

            String fileType =
                    file.getContentType();

            Long fileSize =
                    file.getSize();

            // Upload folder
            Path uploadPath =
                    Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // File Path
            Path filePath =
                    uploadPath.resolve(fileName);

            // Save File
            file.transferTo(filePath);

            // Save Attachment
            Attachment attachment =
                    new Attachment();

            attachment.setFileName(fileName);
            attachment.setFileType(fileType);
            attachment.setFileSize(fileSize);
            attachment.setFilePath(
                    filePath.toString());

            attachment.setIssue(issue);
            attachment.setUploadedBy(currentUser);

            attachment =
                    attachmentRepository.save(
                            attachment);

            return mapToResponse(attachment);

        } catch (IOException e) {

            throw new BusinessException(
                    "Failed to upload file.");
        }
    }


    @Override
    public List<AttachmentResponse> getAttachmentsByIssue(
            Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Issue not found"));

        organizationSecurityService
                .validateIssueAccess(issue);

        List<Attachment> attachments =
                attachmentRepository
                        .findByIssueId(issueId);

        return attachments.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public AttachmentResponse deleteAttachment(
            Long attachmentId) {

        Attachment attachment =
                attachmentRepository
                        .findById(attachmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment not found"));

        organizationSecurityService
                .validateIssueAccess(
                        attachment.getIssue());

        AttachmentResponse response =
                mapToResponse(attachment);

        attachmentRepository.delete(attachment);

        return response;
    }


    @Override
    public Resource downloadAttachment(
            Long attachmentId) {

        try {

            Attachment attachment =
                    attachmentRepository
                            .findById(attachmentId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Attachment not found"));

            organizationSecurityService
                    .validateIssueAccess(
                            attachment.getIssue());

            // It will fetch the file path...
            Path filePath =
                    Paths.get(
                            attachment.getFilePath());

            // Convert Path into Resource...
            Resource resource =
                    new UrlResource(
                            filePath.toUri());

            // Check file available or not...
            if (!resource.exists()) {

                throw new ResourceNotFoundException(
                        "File not found");
            }

            return resource;

        } catch (MalformedURLException e) {

            throw new BusinessException(
                    "Error reading file");
        }
    }


    public AttachmentResponse mapToResponse(
            Attachment attachment) {

        return new AttachmentResponse(
                attachment.getId(),
                attachment.getFileName(),
                attachment.getFileType(),
                attachment.getFileSize(),
                attachment.getUploadedAt(),
                attachment.getUploadedBy().getName()
        );
    }
}