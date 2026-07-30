package com.sprintflow.service;

import com.sprintflow.dto.AttachmentResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface AttachmentService {

    AttachmentResponse uploadAttachment(Long issueId, MultipartFile file);

    List<AttachmentResponse> getAttachmentsByIssue(Long issueId);

    AttachmentResponse deleteAttachment(Long attachmentId);

   // AttachmentResponse updateAttachment(Long attachmentId);

    Resource downloadAttachment(Long attachmentId);

}
