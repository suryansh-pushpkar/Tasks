package com.friendbook.dto;

import java.util.List;

public record HomeResponse(
    UserCardResponse currentUser,
    List<UserCardResponse> searchResults,
    List<NotificationResponse> notifications,
    List<PostResponse> feedPosts
) {
}
