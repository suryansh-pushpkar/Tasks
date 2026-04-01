package com.friendbook.dto;

public record CommentResponse(
    Long id,
    String userName,
    String content,
    String createdAt
) {
}
