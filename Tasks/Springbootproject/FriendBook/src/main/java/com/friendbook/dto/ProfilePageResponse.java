package com.friendbook.dto;

import java.util.List;

public record ProfilePageResponse(
    UserCardResponse currentUser,
    UserCardResponse user,
    boolean ownProfile,
    boolean following,
    boolean pendingRequest,
    long postCount,
    long followerCount,
    long followingCount,
    String favSongs,
    String favBooks,
    String favPlaces,
    List<UserCardResponse> followers,
    List<UserCardResponse> followingUsers,
    List<PostResponse> posts
) {
}
