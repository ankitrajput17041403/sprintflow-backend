package com.sprintflow.service.impl;

import com.sprintflow.dto.NotificationResponse;
import com.sprintflow.entity.Notification;
import com.sprintflow.entity.User;
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

        User currentUser = currentUserService.getCurrentUser();

        List<Notification> notifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(currentUser.getId());

        return notifications.stream().map(notific->mapToResponse(notific)).toList();
    }

    @Override
    public void markAsRead(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification NOT Found"));
        User currentUser = currentUserService.getCurrentUser();

        if(!notification.getRecipient().getId().equals(currentUser.getId())){
            throw new RuntimeException("Access Denied");
        }

        notification.setRead(true);
        notificationRepository.save(notification);

    }
    @Override
    public void createNotification(User recipient, String message) {

        System.out.println("===== CREATE NOTIFICATION CALLED =====");
        System.out.println("Recipient ID: " + recipient.getId());
        System.out.println("Message: " + message);

        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .build();

        notificationRepository.save(notification);

        System.out.println("===== NOTIFICATION SAVED =====");
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


