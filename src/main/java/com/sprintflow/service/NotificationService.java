package com.sprintflow.service;

import com.sprintflow.dto.NotificationResponse;
import com.sprintflow.entity.User;

import java.util.List;


public interface NotificationService {

    List<NotificationResponse> getMyNotifications();

    void markAsRead(Long notificationId);

    void createNotification(User recipient, String message);
    Long getUnreadCount();}