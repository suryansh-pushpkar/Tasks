package com.friendbook.dto;

public record NotificationResponse(
    Long id,
    UserCardResponse sender,
    boolean alreadyFollowingSender,
    String createdAt
) {
}
