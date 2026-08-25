package com.sprintflow.service.impl;

import com.sprintflow.dto.NotificationResponse;
import com.sprintflow.entity.Notification;
import com.sprintflow.entity.User;
import com.sprintflow.exception.AccessDeniedException;
import com.sprintflow.exception.ResourceNotFoundException;
import com.sprintflow.repository.NotificationRepository;
import com.sprintflow.service.CurrentUserService;
import com.sprintflow.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CurrentUserService currentUserService;


    @Override
    public List<NotificationResponse> getMyNotifications() {

        User currentUser =
                currentUserService.getCurrentUser();

        List<Notification> notifications =
                notificationRepository
                        .findByRecipientIdOrderByCreatedAtDesc(
                                currentUser.getId());

        return notifications.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public void markAsRead(Long notificationId) {

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"));

        User currentUser =
                currentUserService.getCurrentUser();

        if (!notification.getRecipient()
                .getId()
                .equals(currentUser.getId())) {

            throw new AccessDeniedException(
                    "Access denied");
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }


    @Override
    public void createNotification(
            User recipient,
            String message) {

        Notification notification =
                Notification.builder()
                        .recipient(recipient)
                        .message(message)
                        .build();

        notificationRepository.save(notification);
    }


    @Override
    public Long getUnreadCount() {

        User currentUser =
                currentUserService.getCurrentUser();

        return notificationRepository
                .countByRecipientIdAndReadFalse(
                        currentUser.getId());
    }


    private NotificationResponse mapToResponse(
            Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}