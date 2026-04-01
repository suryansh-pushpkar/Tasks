package com.friendbook.dto;

import java.util.List;

public record PostResponse(
    Long id,
    UserCardResponse author,
    String caption,
    String imagePath,
    String createdAt,
    int likeCount,
    List<CommentResponse> comments
) {
}
