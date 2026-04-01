package com.friendbook.dto;

public class ProfileResponse {

    private final String message;
    private final String username;
    private final String fullName;
    private final String email;
    private final String favSongs;
    private final String favBooks;
    private final String favPlaces;
    private final String profileImage;

    public ProfileResponse(String message, String username, String fullName, String email,
                           String favSongs, String favBooks, String favPlaces, String profileImage) {
        this.message = message;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.favSongs = favSongs;
        this.favBooks = favBooks;
        this.favPlaces = favPlaces;
        this.profileImage = profileImage;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFavSongs() {
        return favSongs;
    }

    public String getFavBooks() {
        return favBooks;
    }

    public String getFavPlaces() {
        return favPlaces;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
