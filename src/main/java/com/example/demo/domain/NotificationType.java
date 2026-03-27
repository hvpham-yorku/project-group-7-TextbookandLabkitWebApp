package com.example.demo.domain;

/**
 * Classifies what triggered a Notification.
 * KAN-97: initial enum creation.
 *
 * MESSAGE     – someone sent the recipient a contact message about a listing.
 * TRANSACTION – a listing the user is involved with changed state (e.g. marked Sold).
 * SYSTEM      – a platform-level notice (e.g. account update, admin announcement).
 */
public enum NotificationType {
    MESSAGE,
    TRANSACTION,
    SYSTEM
}
