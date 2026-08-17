package com.sprintflow.controller;

import com.sprintflow.dto.NotificationResponse;
import com.sprintflow.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {

        return ResponseEntity.ok(
                notificationService.getMyNotifications()
        );
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId) {

        notificationService.markAsRead(notificationId);

        return ResponseEntity.noContent().build();
    }
}