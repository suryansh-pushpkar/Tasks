package com.friendbook.dto;

public record UserCardResponse(
    Long id,
    String userName,
    String fullName,
    String profileImage
) {
}
